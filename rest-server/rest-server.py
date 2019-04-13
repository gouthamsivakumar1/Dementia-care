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
  'Temp_0',
  'Temp_1',
  'Temp_2',
  'Bat_VTG',
  'RSSI',
  'Lat',
  'Longi',
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

class Patient(Resource):
    def get(self):
        conn = db_connect.connect() # connect to database
        query = conn.execute("select * from PATIENT_LIVE_DATA") # This line performs query and returns json result
        try:
            l = query.cursor.fetchall()
            db_list = convert_db_to_json(l, 'all')
            #return {'all': db_list}
            return db_list
        except Exception as e:
            print("Exception at patient all get: ", str(e))
    
class PatientName(Resource):
    def get(self, patient_id):
        conn = db_connect.connect() # connect to database
        query = conn.execute("select * from PATIENT_LIVE_DATA")
        l = query.cursor.fetchall()
        db_list = convert_db_to_json(l, patient_id)
        return db_list

class PatientLogin(Resource):
    def get(self, patient_id, patient_passwd):
        conn = db_connect.connect() # connect to database
        query = conn.execute("select * from PATIENT_INFO_DB")
        l = query.cursor.fetchall()
        for i in l:
            if patient_id in i and patient_passwd in i:
                return [{'Patient_Id': i[3]}]
        return [{"Patient_Id": "unknown"}]

api.add_resource(Patient, '/all')
api.add_resource(PatientName, '/patient/<patient_id>')
api.add_resource(PatientLogin, '/patient/<patient_id>/<patient_passwd>')


if __name__ == '__main__':
     app.run()

