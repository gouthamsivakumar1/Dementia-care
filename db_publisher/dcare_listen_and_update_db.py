import time
import paho.mqtt.client as paho

import json
import sqlite3

# SQLite DB Name
DB_Name =  "dcare_live.db"

broker="localhost"

Table_schema_elements= """
  Date_n_Time text,
  HospitalId text,
  ReaderId text,
  Patient_Id text,
  Calorie text,
  HeartBeat text,
  StepCount text,
  Bat_VTG text,
  RSSI text,
  FallDetection text
"""

#===============================================================
# Database Manager Class

class DatabaseManager():
    def __init__(self):
        self.conn = sqlite3.connect(DB_Name)
        self.conn.execute('pragma foreign_keys = on')
        self.conn.commit()
        self.cur = self.conn.cursor()

    def add_del_update_db_record(self, sql_query, args=()):
        print("sql_query= ", sql_query)
        print("args = ", args)
        self.cur.execute(sql_query, args)
        self.conn.commit()
        return

    def __del__(self):
        self.cur.close()
        self.conn.close()

#===============================================================
# Functions to push Sensor Data into Database

def patient_data_handler(jsonData):
    #Parse Data 
    sensor_data = json.loads(jsonData)
    dbObj = DatabaseManager()
    try:
        dbObj.add_del_update_db_record("INSERT INTO PATIENT_LIVE_DATA (\
  Date_n_Time,\
  HospitalId,\
  ReaderId,\
  Patient_Id,\
  Calorie,\
  HeartBeat,\
  StepCount,\
  Bat_VTG,\
  RSSI,\
  FallDetection\
  ) VALUES(?,?,?,?,?,?,?,?,?,?)",[
    sensor_data['Date_n_Time'], 
    sensor_data['HospitalId'],
    sensor_data['ReaderId'],   
    sensor_data['Patient_Id'],
    sensor_data['Calorie'],
    sensor_data['HeartBeat'],
    sensor_data['StepCount'],
    sensor_data['Bat_VTG'],
    sensor_data['rssi'],
    sensor_data['FallDetection']
    ])
        del dbObj
        print("patient Data into Database.")
            
        print("")
    except Exception as e:
        print("Exception in db insert = ", str(e))

def sensor_Data_Handler(Topic, jsonData):
    print("received sensor_Data_Handler... got it")
    patient_data_handler(jsonData)

def on_message(client, userdata, msg):
    time.sleep(1)
    try:
        print("received message =",str(msg.payload.decode("utf-8")))
        print("sensor_Data_Handler db calling ")
        sensor_Data_Handler(msg.topic, msg.payload)
    except Exception as e:
        print("except Exception as e :", str(e))
client= paho.Client("client-001")
######Bind function to callback
client.on_message=on_message
#####
print("connecting to broker ",broker)
client.connect(broker)#connect
print("subscribing ")
client.subscribe("dementia_care")#subscribe
client.loop_forever() #start loop to process received messages
