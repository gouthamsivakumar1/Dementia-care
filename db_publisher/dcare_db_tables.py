import sqlite3

# SQLite DB Name
DB_Name =  "dcare_live.db"

# SQLite DB Table Schema

TableSchema="""
drop table if exists PATIENT_LIVE_DATA ;
create table  PATIENT_LIVE_DATA (
  Id integer primary key autoincrement,
  Date_n_Time text,
  HospitalId text,
  ReaderId text,
  Patient_Id text,
  Temp_0 text,
  Temp_1 text,
  Temp_2 text,
  Bat_VTG text,
  RSSI text,
  Lat text,
  Longi text,
  FallDetection text
);

drop table if exists PATIENT_INFO_DB;
create table PATIENT_INFO_DB(
  id integer primary key autoincrement,
  Date_n_Time text,
  Patient_Name text,
  Patient_Id text,
  Patient_Passwd text
);
"""

#Connect or Create DB File
conn = sqlite3.connect(DB_Name)
curs = conn.cursor()

#Create Tables
sqlite3.complete_statement(TableSchema)
curs.executescript(TableSchema)

#Close DB
curs.close()
conn.close()
