import json

from flask import Response

from config import app, mydb, myconnect
from flask import request
from common.models import Event
import os


@app.route("/create_event", methods=["POST"])
def create_event():
    photo = request.files['photo'] if 'photo' in request.files else None
    if photo and not photo.filename:
        photo = None
    photo_type = os.path.splitext(photo.filename)[1] if photo else ""
    event = Event(**request.json, photo_type=photo_type)
    if photo:
        photo.save(os.path.join("/photos/", event.id))

    query = '''insert into events
        (id, name, short_description, event_types, date, time, full_description, address, mail, link, photo)
        values (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
    '''

    print(event.to_dataraw())
    mydb.execute(query, event.to_dataraw())
    myconnect.commit()
    return event.to_primitive()


@app.route("/get_events_list", methods=["GET"])
def get_events_list():
    query = '''select * from events'''
    mydb.execute(query)
    query_result = mydb.fetchall()
    result = [Event(*template).to_dict() for template in query_result]
    print(result)
    return Response(json.dumps(result), mimetype='application/json')
