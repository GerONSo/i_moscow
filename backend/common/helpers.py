import json
import random


def generate_id():
    id_ = ""
    for i in range(10):
        id_ += str(random.randint(0, 9))
    return id_


def generate_cookie():
    cookie = ""
    for i in range(25):
        cookie += str(random.randint(0, 9))
    return cookie


def decode_object(object, default_type: type):
    if object is None:
        return default_type()
    if type(object) == str:
        return json.loads(object)
    return object
