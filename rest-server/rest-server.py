#!/usr/bin/python
from flask import Flask, request, jsonify
from flask_restful import Resource, Api
from sqlalchemy import create_engine
from json import dumps
from collections import OrderedDict 
from dcare_db_format import db_live_data_format
db_connect = create_engine('sqlite:///../db_publisher/dcare_live.db')
app = Flask(__name__)
api = Api(app)


def convert_db_to_json(db_list, field=None):
    new_list = list()
    for record in db_list:
        d = OrderedDict()
        for idx in range(0, len(db_live_data_format)):
        #for idx, column in enumurate(schema_list):
            d[db_live_data_format[idx]] = record[idx+1]
        #print("d[schema_list[idx]] = ", d)
        #print("field = ", field)
        if field == 'all':
            new_list.append(d)
        else:
            if field in d.values():
                new_list.append(d)
        #print("new_list=", new_list)
    return new_list

class PatientAll(Resource):
    def get(self, cursor):
        conn = db_connect.connect() # connect to database
        query = conn.execute("select * from PATIENT_LIVE_DATA") # This line performs query and returns json result
        try:
            l = query.cursor.fetchall()
            new_list = l[int(cursor):]
            db_list = convert_db_to_json(new_list, 'all')
            #return {'all': db_list}
            return db_list
        except Exception as e:
            print("Exception at patient all get: ", str(e))
    
class PatientInfoFromId(Resource):
    def get(self, patient_id, cursor):
        conn = db_connect.connect() # connect to database
        query = conn.execute("select * from PATIENT_LIVE_DATA")
        l = query.cursor.fetchall()
        new_list = l[int(cursor):]
        db_list = convert_db_to_json(l, patient_id) 
        new_list_1 = db_list[int(cursor):]
        print("db_list=", db_list, "patient_id = ", patient_id, "cursor=",cursor)
        return new_list_1

def get_patient_name_from_category(patient_id, patient_name):
    conn = db_connect.connect() # connect to database
    query = conn.execute("select * from PATIENT_INFO_DB")
    l = query.cursor.fetchall()
    for row in l:
        if patient_id in row and "bystander" not in row:
            return row[2]
    return 'unknown'

class PatientLogin(Resource):
    def get(self, patient_id, patient_passwd):
        conn = db_connect.connect() # connect to database
        query = conn.execute("select * from PATIENT_INFO_DB")
        l = query.cursor.fetchall()
        for i in l:
            if patient_id in i and patient_passwd in i:
                patient_id = i[3]
                user_cat = i[5]
                patient_name = get_patient_name_from_category(patient_id, user_cat)
                return [{'Patient_Id': patient_id,
                         'Patient_Name':patient_name,
                         'User_Category':user_cat
                        }]
        return [{'Patient_Id': 'unknown',
                 'Patient_Name':'unknown',
                 'User_Category': 'unknown'
                }]

api.add_resource(PatientAll, '/all/<cursor>')
api.add_resource(PatientInfoFromId, '/patient/<patient_id>/<cursor>')
api.add_resource(PatientLogin, '/patient-login/<patient_id>/<patient_passwd>')


if __name__ == '__main__':
     app.run("0.0.0.0")

