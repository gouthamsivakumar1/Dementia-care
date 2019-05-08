import time
import paho.mqtt.client as paho
import dcare_db_format as data_format
import json
import sqlite3

# SQLite DB Name
DB_Name =  "dcare_live.db"

broker="localhost"

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
        dbObj.add_del_update_db_record("INSERT INTO PATIENT_LIVE_DATA "
            + data_format.format_sensor_data(sensor_data))
        del dbObj
        print("patient Data into Database.")
            
        print("")
    except Exception as e:
        print("Exception in db insert = ", str(e))

def convert_string_to_list_of_json(input_s):
    multiple_records= input_s.split("##")
    multiple_records= [i for i in multiple_records if len(i)>0]
    list_of_list_record = list()
    for record in multiple_records:
        l = record.split('|')
        list_of_list_record.append(l)

    print(list_of_list_record)

    sensor_data_list = list()
    for record in list_of_list_record:
        sensor_data ={}
        for idx, val in enumerate(data_format.db_live_data_format) :
            sensor_data[val] = record[idx]
        #Do any conversion on data Dictionary
        if sensor_data['ReaderId'] == '008':
            sensor_data['ReaderId'] = 'reader1'
        else:
            sensor_data['ReaderId'] = 'reader2'
        sensor_data['HospitalId'] = 'Stanford Hospital'

        sensor_data_json = json.dumps(sensor_data)
        sensor_data_list.append(sensor_data_json)
    return sensor_data_list

def sensor_Data_Handler(Topic, input_s):
    print("received sensor_Data_Handler... got it")
    json_list = convert_string_to_list_of_json(input_s)
    for j in json_list:
        patient_data_handler(j)

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
