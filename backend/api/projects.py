import json

from flask import Response

import common.actions
from common.models import ChatRoles
from common.models import MessageTypes
from common.repositories import ChatRepository
from common.models import Project
from config import app, mydb, myconnect
from flask import request


@app.route("/create_project/<cookie>", methods=["POST"])
def create_project(cookie):
    user_id = common.actions.make_cookie_authorize(cookie)
    if user_id is None:
        return {}, 401
    account = common.actions.get_account_by_id(user_id)
    project = Project(master_id=account.id, chat_id=ChatRepository(owner_id=account.id).chat.id, **request.json)
    account.master_project_ids.append(project.id)
    # account.chat_ids.append(project.chat_id)

    for tag in project.tags:
        mydb.execute("insert into account_tag_match (id, tag) values (%s, %s)", (project.id, tag))
    query = '''
        insert into projects 
            (id, name, master_id, chat_id, slaves_id, photo, metadata)
            values (%s, %s, %s, %s, %s, %s, %s)
    '''
    mydb.execute(query, project.to_dataraw(skip_fields=["tags"]))
    mydb.execute("update accounts set master_project_ids = %s where id = %s",
                 (json.dumps(account.master_project_ids), account.id))
    myconnect.commit()

    return project.to_primitive(), 201


@app.route("/get_projects_list", methods=["GET"])
def get_projects_list():
    mydb.execute('''select * from projects''')
    query_result = mydb.fetchall()
    print(query_result)
    result = [Project(*template).to_dict() for template in query_result]
    return Response(json.dumps(result), mimetype='application/json')


@app.route("/get_my_master_projects/<cookie>", methods=["GET"])
def get_my_master_projects(cookie):
    user_id = common.actions.make_cookie_authorize(cookie)
    if user_id is None:
        return {}, 401
    account = common.actions.get_account_by_id(user_id)
    projects = [common.actions.get_project_by_id(id_) for id_ in account.master_project_ids]
    result = [project.to_dict() for project in projects]
    return Response(json.dumps(result), mimetype='application/json')


@app.route("/get_my_slave_projects/<cookie>", methods=["GET"])
def get_my_slave_projects(cookie):
    user_id = common.actions.make_cookie_authorize(cookie)
    if user_id is None:
        return {}, 401
    account = common.actions.get_account_by_id(user_id)
    projects = [common.actions.get_project_by_id(id_) for id_ in account.slave_project_ids]
    result = [project.to_dict() for project in projects]
    return Response(json.dumps(result), mimetype='application/json')


@app.route("/get_project_by_id", methods=["GET"])
def get_project_by_id():
    project = common.actions.get_project_by_id(request.json["id"])
    return project.to_primitive()


@app.route("/join_to_project/<cookie>", methods=["POST"])
def join_to_project(cookie):
    user_id = common.actions.make_cookie_authorize(cookie)
    project_id = request.json["project_id"]
    if user_id is None:
        return {}, 401

    account = common.actions.get_account_by_id(user_id)
    project = common.actions.get_project_by_id(project_id)
    if project is None:
        return {}, 400
    if account.id in project.slaves_id or account.id == project.master_id:
        return {}, 400

    project.slaves_id.append(account.id)
    account.slave_project_ids.append(project.id)
    mydb.execute("update accounts set slave_project_ids = %s where id = %s",
                 (json.dumps(account.slave_project_ids), account.id))
    mydb.execute("update projects set slaves_id = %s where id = %s", (json.dumps(project.slaves_id), project.id))
    myconnect.commit()
    return project.to_primitive()


@app.route("/accept_to_project/<cookie>", methods=["POST"])
def accept_to_project(cookie):
    user_id = common.actions.make_cookie_authorize(cookie)
    project_id, slave_id = request.json["project_id"], request.json["slave_id"]
    if user_id is None:
        return {}, 401

    account = common.actions.get_account_by_id(user_id)
    slave = common.actions.get_account_by_id(slave_id)
    project = common.actions.get_project_by_id(project_id)
    if project is None or slave is None:
        return {}, 400
    if slave.id in project.slaves_id or account.id != project.master_id:
        return {}, 400

    project.slaves_id.append(slave.id)
    slave.slave_project_ids.append(project.id)
    mydb.execute("update accounts set slave_project_ids = %s where id = %s",
                 (json.dumps(slave.slave_project_ids), slave.id))
    mydb.execute("update projects set slaves_id = %s where id = %s", (json.dumps(project.slaves_id), project.id))
    myconnect.commit()
    return project.to_primitive()


@app.route("/send_project_request/<cookie>", methods=["POST"])
def send_project_request(cookie):
    user_id = common.actions.make_cookie_authorize(cookie)
    project_id = request.json["project_id"]
    if user_id is None:
        return {}, 401

    account = common.actions.get_account_by_id(user_id)
    project = common.actions.get_project_by_id(project_id)
    if project is None:
        return {}, 400
    if account.id in project.slaves_id or account.id == project.master_id:
        return {}, 400

    chat = ChatRepository(id=project.chat_id)
    chat.join_chat(member_id=account.id, member_role=ChatRoles.UNAPPROVED)
    # account.chat_ids.append(chat.chat.id)
    # mydb.execute("update accounts set chat_ids = %s where id = %s",
    #              (json.dumps(account.chat_ids), account.id))
    # myconnect.commit()
    chat.send_message(person_id=user_id, message_type=MessageTypes.INVITE)
    return {}  # change


# @app.route("/send_slave_request/<cookie>", methods=["POST"])
# def send_slave_request(cookie):
#     user_id = common.actions.make_cookie_authorize(cookie)
#     slave_id = request.json["slave_id"]
#     if user_id is None:
#         return {}, 401
#
#     account = common.actions.get_account_by_id(user_id)
#     slave = common.actions.get_account_by_id(slave_id)
#     if slave is None:
#         return {}, 400
#     if account.id in project.slaves_id or account.id == project.master_id:
#         return {}, 400
#
#     chat = ChatRepository(id=project.chat_id)
#     chat.join_chat(member_id=account.id, member_role=ChatRoles.UNAPPROVED)
#     # account.chat_ids.append(chat.chat.id)
#     # mydb.execute("update accounts set chat_ids = %s where id = %s",
#     #              (json.dumps(account.chat_ids), account.id))
#     # myconnect.commit()
#     return {}  # change