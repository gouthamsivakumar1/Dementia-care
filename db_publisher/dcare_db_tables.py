#!/usr/bin/python
#sys.exit(0)
import sqlite3
import sys
import dcare_db_format as data_format
# SQLite DB Name
DB_Name =  "dcare_live.db"

# SQLite DB Table Schema
#'''
#TableSchema="""
#drop table if exists PATIENT_LIVE_DATA ;
#create table  PATIENT_LIVE_DATA (
#  Id integer primary key autoincrement,
#  HospitalId text,
#  ReaderId text,
#  Date_n_Time text,
#  HeartBeat text,
#  Patient_Id text,
#  StepCount text,
#  Calorie text,
#  Temparature text,
#  FallDetection text
#  Bat_VTG text,
#  RSSI text,
#);

#drop table if exists PATIENT_INFO_DB;
#create table PATIENT_INFO_DB(
#  id integer primary key autoincrement,
#  Date_n_Time text,
#  Patient_Name text,
#  Patient_Id text,
#  Patient_Passwd text,
#  User_Category text
#);
#"""
#'''
#print(data_format.fill_patient_live_data_db_table())
TableSchema= "\
drop table if exists PATIENT_LIVE_DATA ;\
create table  PATIENT_LIVE_DATA (\
  Id integer primary key autoincrement,"\
 + data_format.fill_patient_live_data_db_table() + ");" +\
"\
drop table if exists PATIENT_INFO_DB;\
create table PATIENT_INFO_DB(\
  id integer primary key autoincrement,\
" + data_format.fill_patient_info_db() + ");"

#Connect or Create DB File
#import pdb;pdb.set_trace()
print(TableSchema)
conn = sqlite3.connect(DB_Name)
curs = conn.cursor()
#Create Tables
sqlite3.complete_statement(TableSchema)
curs.executescript(TableSchema)

#Close DB
curs.close()
conn.close()
