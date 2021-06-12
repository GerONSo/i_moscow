import json

import common.actions

from common import generate_id


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
                 snils="",
                 description="",
                 links=None,
                 photo_type="/photos/xs.jpg",
                 my_events=None,
                 master_project_ids=None,
                 slave_project_ids=None,
                 tags=None):
        if id is None:
            id = generate_id()
        if tags is None:
            tags = common.actions.get_tags_by_account(id)
        if links is None:
            links = []
        if master_project_ids is None:
            master_project_ids = []
        if type(master_project_ids) == str:
            master_project_ids = json.loads(master_project_ids)
        if slave_project_ids is None:
            slave_project_ids = []
        if type(slave_project_ids) == str:
            slave_project_ids = json.loads(slave_project_ids)
        if type(links) == str:
            links = json.loads(links)
        if my_events is None:
            my_events = []
        if type(my_events) == str:
            my_events = json.loads(my_events)
        self.id = id
        self.name = name
        self.mail = mail
        self.password = password
        self.snils = snils
        self.description = description
        self.links = links
        self.tags = tags
        self.photo_link = photo_type
        self.my_events = my_events
        self.master_project_ids = master_project_ids
        self.slave_project_ids = slave_project_ids


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
        if master_id is None:
            raise ValueError
        if chat_id is None:
            chat_id = generate_id()
        if slaves_id is None:
            slaves_id = []
        if type(slaves_id) == str:
            slaves_id = json.loads(slaves_id)
        if metadata is None:
            metadata = dict()
        if type(metadata) == str:
            metadata = json.loads(metadata)
        if tags is None:
            tags = common.actions.get_tags_by_account(id)

        self.id = id
        self.name = name
        self.master_id = master_id
        self.chat_id = chat_id
        self.slaves_id = slaves_id
        self.photo_link = photo_type
        self.metadata = metadata
        self.tags = tags
