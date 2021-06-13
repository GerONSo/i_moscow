import json

from flask import Response

import common.actions
from common.repositories import ChatRepository
from config import app, mydb, myconnect
from flask import request


@app.route("/get_messages_list/<cookie>", methods=["GET"])
def get_messages_list(cookie):
    user_id = common.actions.make_cookie_authorize(cookie)
    if user_id is None:
        return {}, 401

    chat = ChatRepository(id=request.json["chat_id"])
    if user_id not in chat.member_ids():
        return {}, 403
    result = [message.to_dict() for message in chat.get_messages(person_id=user_id)]
    return Response(json.dumps(result), mimetype='application/json')


@app.route("/send_message/<cookie>", methods=["POST"])
def send_message(cookie):
    user_id = common.actions.make_cookie_authorize(cookie)
    if user_id is None:
        return {}, 401

    chat = ChatRepository(id=request.json["chat_id"])
    if user_id not in chat.member_ids():
        return {}, 403

    message_type = request.json["message_type"]
    message_metadata = request.json["metadata"]

    message = chat.send_message(person_id=user_id, message_type=message_type, metadata=message_metadata)
    if message is None:
        return {}, 400
    return message.to_primitive()


@app.route("/get_members_list/<cookie>", methods=["GET"])
def get_members_list(cookie):
    user_id = common.actions.make_cookie_authorize(cookie)
    if user_id is None:
        return {}, 401

    chat = ChatRepository(id=request.json["chat_id"])
    if user_id not in chat.member_ids():
        return {}, 403
    result = chat.get_members(person_id=user_id)
    return Response(json.dumps(result), mimetype='application/json')


@app.route("/get_full_members_list/", methods=["GET"])  # do not use in app
def get_full_members_list():
    chat = ChatRepository(id=request.json["chat_id"])
    result = [member.to_dict() for member in chat.members]
    return Response(json.dumps(result), mimetype='application/json')
