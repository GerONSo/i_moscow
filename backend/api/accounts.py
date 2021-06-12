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
            (id, name, mail, password, snils, description, links, photo)
            values (%s, %s, %s, %s, %s, %s, %s, %s)
    '''
    mydb.execute(query, account.to_dataraw(skip_fields=["tags"]))
    myconnect.commit()

    return account.to_primitive(), 201


@app.route('/authorise', methods=["POST"])
def authorise():
    id_, password = request.json["id"], request.json["password"]
    account = common.actions.get_account_by_id(id_)
    if not account:
        return {}, 400
    if account.password != password:
        return {}, 403

    mydb.execute("delete from session where id = %s", (id_, ))
    cookie = generate_cookie()
    mydb.execute("insert into session (id, cookie) values (%s, %s)", (account.id, cookie))

    return Response(json.dumps({"cookie": cookie, "account": account.to_dict()}), mimetype='application/json')
