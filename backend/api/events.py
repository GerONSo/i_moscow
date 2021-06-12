import json

from flask import Response

from config import app, mydb, myconnect
from flask import request
from common.models import Event
import os


@app.route("/create_event", methods=["POST"])
def create_event():
    event = Event(**request.json)
    query = '''insert into events
        (id, name, short_description, event_types, date, time, full_description, address, mail, link, photo)
        values (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
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
