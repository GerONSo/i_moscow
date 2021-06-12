import random


def generate_id():
    id_ = ""
    for i in range(10):
        id_ += str(random.randint(0, 9))
    return id_