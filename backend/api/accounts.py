import json

from flask import Response

import common.actions
from config import app, mydb, myconnect
from flask import request
from common.models import Account
from common import generate_cookie


@app.route('/create_account', methods=["POST"])
def create_account():
    account = Account(**request.json)
    mydb.execute("select * from accounts where id = %s", (account.id, ))
    if mydb.fetchall():
        return {}, 400

    for tag in account.tags:
        mydb.execute("insert into account_tag_match (id, tag) values (%s, %s)", (account.id, tag))
    query = '''
        insert into accounts 
            (id, name, mail, password, photo, my_events, master_project_ids, slave_project_ids, 
             chat_ids, notifications, metadata)
            values (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
    '''
    mydb.execute(query, account.to_dataraw(skip_fields=["tags"]))
    myconnect.commit()

    return account.to_primitive(), 201


@app.route('/authorize', methods=["POST"])
def authorize():
    id_, password = request.json["id"], request.json["password"]
    account = common.actions.get_account_by_id(id_)
    if not account:
        return {}, 400
    if account.password != password:
        return {}, 403

    mydb.execute("delete from session where id = %s", (id_, ))
    cookie = generate_cookie()
    mydb.execute("insert into session (id, cookie) values (%s, %s)", (account.id, cookie))
    myconnect.commit()

    return Response(json.dumps({"cookie": cookie}), mimetype='application/json')


@app.route("/get_my_account/<cookie>", methods=["GET"])
def get_my_account(cookie):
    user_id = common.actions.make_cookie_authorize(cookie)
    if user_id is None:
        return {}, 401
    account = common.actions.get_account_by_id(user_id)
    return account.to_primitive()


@app.route("/get_account_by_id", methods=["GET"])
def get_account_by_id():
    account = common.actions.get_account_by_id(request.json["id"])
    return account.to_primitive()


@app.route("/update_my_account/<cookie>", methods=["POST"])
def update_my_account(cookie):
    user_id = common.actions.make_cookie_authorize(cookie)
    if user_id is None:
        return {}, 401
    account = Account(**request.json)
    if user_id != account.id:
        return {}, 403

    mydb.execute("delete from account_tag_match where id = %s", (user_id, ))
    for tag in account.tags:
        mydb.execute("insert into account_tag_match (id, tag) values (%s, %s)", (account.id, tag))

    query = '''
            update accounts set
                name = %s, mail = %s, password = %s, photo = %s, my_events = %s, master_project_ids = %s,
                slave_project_ids = %s, chat_ids = %s, notifications = %s, metadata = %s where id = %s
        '''
    mydb.execute(query, account.to_dataraw(skip_fields=["id", "tags"]) + (account.id, ))
    myconnect.commit()
    return account.to_primitive()


@app.route("/get_my_chats/<cookie>", methods=["GET"])
def get_my_chats(cookie):
    user_id = common.actions.make_cookie_authorize(cookie)
    if user_id is None:
        return {}, 401
    account = common.actions.get_account_by_id(user_id)
    chats = [common.actions.get_chat_by_id(id_) for id_ in account.chat_ids]
    result = [chat.to_dict() for chat in chats]
    return Response(json.dumps(result), mimetype='application/json')


@app.route("/get_accounts_list", methods=["GET"])
def get_accounts_list():
    mydb.execute('''select * from accounts''')
    query_result = mydb.fetchall()
    result = [Account(*template).to_dict() for template in query_result]
    return Response(json.dumps(result), mimetype='application/json')