from common.models import Account
from common.models import Event
from config import mydb


def make_cookie_authorise(cookie):
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


def get_account_by_id(id_):
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
