import json
from common import generate_id


class BaseEncoder(json.JSONEncoder):
    def default(self, o):
        return o.__dict__


class BaseModel:
    def to_primitive(self):
        return json.dumps(self, cls=BaseEncoder)
    def to_dict(self):
        return BaseEncoder().default(self)
    def to_list(self):
        return [i[1] for i in list(self.to_dict().items())]
    def keys_list(self):
        return [i[0] for i in list(self.to_dict().items())]
    def to_dataraw(self):
        # idite nahuy yebaniy mysql
        res = self.to_list()
        for i in range(len(res)):
            if type(res[i]) == list:
                res[i] = ", ".join(res[i])
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
                 photo_type=""):
        if id_ is None:
            id_ = generate_id()
        if event_types is None:
            event_types = []
        if type(event_types) == str:
            event_types = event_types.split(", ") if event_types != "" else []
        print(event_types, type(event_types))
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
        self.photo_link = "/photos/{}.{}".format(self.id, photo_type) if photo_type else ""
