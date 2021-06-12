from config import app
from flask import send_file
import os


@app.route('/photos/<photo_id>')
def get_photo(photo_id):
    return send_file("photos/{}".format(photo_id), mimetype='image/png-jpeg-pdf-jpg')
