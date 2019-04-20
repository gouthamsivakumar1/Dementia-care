import time
from datetime import datetime
import sys 
import paho.mqtt.client as paho

import argparse
import json
import sqlite3

# SQLite DB Name
DB_Name =  "dcare_live.db"

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

def patient_database_handler(u_name, u_id, u_pass, u_category):
    dbObj = DatabaseManager()
    try:
        date_time = datetime.today().strftime("%d-%b-%Y %H:%M:%S:%f")

        dbObj.add_del_update_db_record("INSERT INTO PATIENT_INFO_DB (\
  Date_n_Time,\
  Patient_Name,\
  Patient_Id,\
  Patient_Passwd,\
  User_Category\
  ) VALUES(?,?,?,?,?)",[
     date_time,
     u_name,
     u_id,
     u_pass,
     u_category
    ])
        del dbObj
        print("patient Data into Database.")
            
    except Exception as e:
        print("Exception in db insert = ", str(e))

parser = argparse.ArgumentParser()
parser.add_argument("--user", help="add user",
                            type=str, nargs=1)
parser.add_argument("--id", help="add user id",
                            type=str, nargs=1)
parser.add_argument("--passwd", help="add user passwd",
                            type=str, nargs=1)
parser.add_argument("--category", help="add user category",
                                    default='patient', const='patient', choices=['patient',
                                        'bystander', 'admin'] , nargs='?')
args = parser.parse_args()
if (args.user and args.id and args.passwd):
    print("args.category=", args.category)
    patient_database_handler(args.user[0], args.id[0], args.passwd[0],
            args.category)
else:
    parser.print_help(sys.stderr)
