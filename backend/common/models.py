import json

import common.actions

from common import generate_id, decode_object


class ChatRoles:
    OWNER = "owner"
    DEFAULT = "default"
    UNAPPROVED = "unapproved"


class MessageTypes:
    DEFAULT = "default"
    INVITE = "invite"


class ChatTypes:
    PERSON = "person"
    GROUP = "group"


class BaseEncoder(json.JSONEncoder):
    def default(self, o):
        return o.__dict__


class BaseModel:
    def to_primitive(self):
        return json.dumps(self, cls=BaseEncoder)
    def to_dict(self) -> dict:
        return BaseEncoder().default(self)
    def to_list(self):
        return [i[1] for i in list(self.to_dict().items())]
    def keys_list(self):
        return [i[0] for i in list(self.to_dict().items())]
    def to_dataraw(self, skip_fields=None):
        # idite nahuy yebaniy mysql
        res = []
        for item in self.to_dict().items():
            if item[0] not in (skip_fields or []):
                res.append(item[1])
        for i in range(len(res)):
            if type(res[i]) in (list, dict):
                res[i] = json.dumps(res[i])
        return tuple(res)
    def keys_tuple(self):
        return tuple(self.keys_list())

class Event(BaseModel):
    def __init__(self,
                 id_=None,
                 name="Common",
                 short_description="",
                 event_types=None,
                 date="",
                 time="",
                 full_description="",
                 address="",
                 mail="",
                 link="",
                 photo_type="/photos/xs.jpg",
                 participants=None,
                 department="",
                 creator=""):
        if id_ is None:
            id_ = generate_id()
        if event_types is None:
            event_types = []
        if participants is None:
            participants = []
        if type(event_types) == str:
            event_types = json.loads(event_types)
        if type(participants) == str:
            participants = json.loads(participants)
        self.id = id_
        self.name = name
        self.short_description = short_description
        self.event_types = event_types
        self.date = date
        self.time = time
        self.full_description = full_description
        self.address = address
        self.mail = mail
        self.link = link
        self.photo_link = photo_type
        self.participants = participants
        self.department = department
        self.creator = creator


class Account(BaseModel):
    def __init__(self,
                 id=None,
                 name="Common",
                 mail="",
                 password="",
                 photo_type="/photos/xs.jpg",
                 my_events=None,
                 master_project_ids=None,
                 slave_project_ids=None,
                 chat_ids=None,
                 notifications=None,
                 metadata=None,
                 tags=None):
        if id is None:
            id = generate_id()
        if tags is None:
            tags = common.actions.get_tags_by_account(id)
        self.id = id
        self.name = name
        self.mail = mail
        self.password = password
        self.tags = tags
        self.photo_link = photo_type
        self.my_events = decode_object(my_events, list)
        self.master_project_ids = decode_object(master_project_ids, list)
        self.slave_project_ids = decode_object(slave_project_ids, list)
        self.chat_ids = decode_object(chat_ids, list)
        self.notifications = decode_object(notifications, dict)
        self.metadata = decode_object(metadata, dict)


class Project(BaseModel):
    def __init__(self,
                 id=None,
                 name="Common",
                 master_id=None,
                 chat_id=None,
                 slaves_id=None,
                 photo_type="/photos/xs.jpg",
                 metadata=None,
                 tags=None):
        if id is None:
            id = generate_id()
        if master_id is None or chat_id is None:
            raise ValueError
        if tags is None:
            tags = common.actions.get_tags_by_account(id)

        self.id = id
        self.name = name
        self.master_id = master_id
        self.chat_id = chat_id
        self.slaves_id = decode_object(slaves_id, list)
        self.photo_link = photo_type
        self.metadata = decode_object(metadata, dict)
        self.tags = tags


class Chat(BaseModel):
    def __init__(self, id=None, chat_type=ChatTypes.PERSON, metadata=None):
        if id is None:
            raise ValueError
        self.id = id
        self.chat_type = chat_type
        self.metadata = decode_object(metadata, dict)


class Member(BaseModel):
    def __init__(self, id=None, role=ChatRoles.DEFAULT):
        if id is None:
            raise ValueError
        self.id = id
        self.role = role


class Message(BaseModel):
    def __init__(self, number=0, id=None, from_user=None, message_type=MessageTypes.DEFAULT, metadata=None):
        if id is None:
            id = generate_id()
        if from_user is None:
            raise ValueError
        if metadata is None:
            metadata = dict()
        if type(metadata) == str:
            metadata = json.loads(metadata)
        self.number = number
        self.id = id
        self.from_user = from_user
        self.message_type = message_type
        self.metadata = metadata
