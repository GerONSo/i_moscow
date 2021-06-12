import json

import common.actions
from common import generate_id, to_table_list


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
            if type(res[i]) == list:
                res[i] = to_table_list(res[i])
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
                 participants=None):
        if id_ is None:
            id_ = generate_id()
        if event_types is None:
            event_types = []
        if participants is None:
            participants = []
        if type(event_types) == str:
            event_types = event_types.split(", ") if event_types != "" else []
        if type(participants) == str:
            participants = participants.split(", ") if participants != "" else []
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
                 tags=None):
        if id is None:
            id = generate_id()
        if tags is None:
            tags = common.actions.get_tags_by_account(id)
        if links is None:
            links = []
        if type(links) == str:
            links = links.split(", ") if links != "" else []
        if my_events is None:
            my_events = []
        if type(my_events) == str:
            my_events = my_events.split(", ") if my_events != "" else []
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
