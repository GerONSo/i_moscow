import json
from flask import request

from config import app
from config import mydb


@app.route('/ping')
def ping():
    return dict(), 200


@app.route('/get_cookie')
def get_cookie():
    id_ = request.json["id"]
    mydb.execute("select * from session where id = %s", (id_, ))
    data = mydb.fetchall()
    return json.dumps(data)
