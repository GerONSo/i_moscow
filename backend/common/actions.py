import json

from common.models import Account
from common.models import Chat
from common.models import Event
from common.models import Project
from config import mydb


def make_cookie_authorize(cookie):
    mydb.execute("select * from session where cookie = %s", (cookie, ))
    data = mydb.fetchall()
    if not data:
        return None
    return data[0][0]


def get_tags_by_account(id_):
    mydb.execute("select (tag) from account_tag_match where id = %s", (id_, ))
    data = mydb.fetchall()
    data = [str(tag[0]) for tag in data]
    return data


def get_account_by_id(id_) -> Account:
    mydb.execute("select * from accounts where id = %s", (id_, ))
    template = mydb.fetchall()
    if not template:
        return None
    return Account(*template[0])


def get_event_by_id(id_):
    mydb.execute("select * from events where id = %s", (id_, ))
    template = mydb.fetchall()
    if not template:
        return None
    return Event(*template[0])


def get_project_by_id(id_):
    mydb.execute("select * from projects where id = %s", (id_, ))
    template = mydb.fetchall()
    if not template:
        return None
    return Project(*template[0])


def get_chat_by_id(id_):
    mydb.execute("select * from chats where id = %s", (id_, ))
    template = mydb.fetchall()
    if not template:
        return None
    return Chat(*template[0])


def get_person_chat_by_members(id1, id2):
    chat = []
    mydb.execute('''select (id) from chats where metadata = %s''', (json.dumps({"ident": (id1, id2)}), ))
    chat += mydb.fetchall()
    mydb.execute('''select (id) from chats where metadata = %s''', (json.dumps({"ident": (id2, id1)}), ))
    chat += mydb.fetchall()
    if not chat:
        return None
    return get_chat_by_id(chat[0][0])
