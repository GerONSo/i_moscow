import json

from flask import Response

import common.actions
from config import app, mydb, myconnect
from flask import request
from common.models import Event
import os


@app.route("/create_event", methods=["POST"])
def create_event():
    event = Event(**request.json)
    query = '''insert into events
        (id, name, short_description, event_types, date, time, full_description, address, mail, link, photo,
         participants, department, creator)
        values (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
    '''

    mydb.execute(query, event.to_dataraw())
    myconnect.commit()
    return event.to_primitive(), 201


@app.route("/get_events_list", methods=["GET"])
def get_events_list():
    query = '''select * from events'''
    mydb.execute(query)
    query_result = mydb.fetchall()
    result = [Event(*template).to_dict() for template in query_result]
    return Response(json.dumps(result), mimetype='application/json')


@app.route("/get_my_events/<cookie>", methods=["GET"])
def get_my_events(cookie):
    user_id = common.actions.make_cookie_authorize(cookie)
    if user_id is None:
        return {}, 401
    account = common.actions.get_account_by_id(user_id)
    events = [common.actions.get_event_by_id(id_) for id_ in account.my_events]
    result = [event.to_dict() for event in events]
    return Response(json.dumps(result), mimetype='application/json')


@app.route("/join_to_event/<cookie>", methods=["POST"])
def join_to_event(cookie):
    user_id = common.actions.make_cookie_authorize(cookie)
    event_id = request.json["event_id"]
    if user_id is None:
        return {}, 401

    account = common.actions.get_account_by_id(user_id)
    event = common.actions.get_event_by_id(event_id)
    if event is None:
        return {}, 400
    if account.id in event.participants:
        return {}, 400
    event.participants.append(account.id)
    account.my_events.append(event.id)
    mydb.execute("update accounts set my_events = %s where id = %s", (json.dumps(account.my_events), account.id))
    mydb.execute("update events set participants = %s where id = %s", (json.dumps(event.participants), event.id))
    myconnect.commit()
    return event.to_primitive()
