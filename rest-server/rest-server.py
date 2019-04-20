from flask import Flask, request, jsonify
from flask_restful import Resource, Api
from sqlalchemy import create_engine
from json import dumps
from collections import OrderedDict 

db_connect = create_engine('sqlite:///../db_publisher/dcare_live.db')
app = Flask(__name__)
api = Api(app)

schema_list = [
  'Date_n_Time',
  'HospitalId',
  'ReaderId',
  'Patient_Id',
  'Calorie',
  'HeartBeat',
  'StepCount',
  'Bat_VTG',
  'RSSI',
  'FallDetection' 
]



def convert_db_to_json(db_list, field=None):
    new_list = list()
    for record in db_list:
        d = OrderedDict()
        for idx in range(0, len(schema_list)):
        #for idx, column in enumurate(schema_list):
            d[schema_list[idx]] = record[idx+1]
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
            print("len(l)=", len(l), "l= ", l)
            print("cursor=", int(cursor))
            print("len(new_list)=", len(new_list), "new_list=", new_list)

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

class PatientLogin(Resource):
    def get(self, patient_id, patient_passwd):
        conn = db_connect.connect() # connect to database
        query = conn.execute("select * from PATIENT_INFO_DB")
        l = query.cursor.fetchall()
        for i in l:
            print("cat=", i[4], " 5= ", i[5])
            if patient_id in i and patient_passwd in i:
                return [{'Patient_Id': i[3], 'User_Category':i[5]}]
        return [{"Patient_Id": "unknown", 'User_Category': 'unknown'}]

api.add_resource(PatientAll, '/all/<cursor>')
api.add_resource(PatientInfoFromId, '/patient/<patient_id>/<cursor>')
api.add_resource(PatientLogin, '/patient-login/<patient_id>/<patient_passwd>')


if __name__ == '__main__':
     app.run()

