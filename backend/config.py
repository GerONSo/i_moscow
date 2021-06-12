from flask import Flask
import mysql.connector

app = Flask(__name__)
myconnect = mysql.connector.connect(
    host="localhost",
    user="NoAnguish",
    password="lolkek",
    database="ai_moscow"
)

mydb = myconnect.cursor()
import api