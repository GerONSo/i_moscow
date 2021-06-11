from config import app

@app.route('/ping')
def ping():
    return dict(), 200