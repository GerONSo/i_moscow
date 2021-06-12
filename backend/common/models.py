import json
from common import generate_id


class BaseEncoder(json.JSONEncoder):
    def default(self, o):
        return o.__dict__


class BaseModel:
    def to_primitive(self):
        return json.dumps(self, cls=BaseEncoder)
    def to_dict(self):
        return self.__dict__
    def to_list(self):
        return [i[1] for i in list(self.to_dict().items())]
    def keys_list(self):
        return [i[0] for i in list(self.to_dict().items())]
    def to_tuple(self):
        return tuple(self.to_list())
    def keys_tuple(self):
        return tuple(self.keys_list())

class Event(BaseModel):
    def __init__(self,
                 id_=generate_id(),
                 name="Common",
                 short_description="",
                 type="",
                 date="",
                 time="",
                 full_description="",
                 address="",
                 mail="",
                 link="",
                 photo_type=""):
        self.id = id_
        self.name = name
        self.short_description = short_description
        self.type = type
        self.date = date
        self.time = time
        self.full_description = full_description
        self.address = address
        self.mail = mail
        self.link = link
        self.photo_link = "/photos/{}.{}".format(self.id, photo_type) if photo_type else ""
