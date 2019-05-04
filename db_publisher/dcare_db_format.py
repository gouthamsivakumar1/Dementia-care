
db_live_data_format = [
  'HospitalId',
  'ReaderId', 
  'Date_n_Time',
  'HeartBeat',
  'Patient_Id',
  'StepCount',
  'Calorie',
  'Temparature',
  'FallDetection',
  'Bat_VTG',
  'RSSI',
]

db_user_data_format = [
  'Date_n_Time',
  'Patient_Name', 
  'Patient_Id',
  'Patient_Passwd',
  'User_Category',
]

def fill_patient_live_data_db_table():
    live_data_text = ""
    for i in db_live_data_format:
        live_data_text = live_data_text + i + " text,"
    return live_data_text[0:-1]

def format_sensor_data(d):
    print("input dict: ", d)
    live_data_text = "("
    for i in db_live_data_format:
        live_data_text = live_data_text + i + ","
    live_data_text = live_data_text[0:-1] + ") VALUES("
    for i in  db_live_data_format:
        live_data_text = live_data_text + "'" + d[i] + "'" + ","
    live_data_text = live_data_text[0:-1] + ")"

    print(live_data_text)
    return live_data_text

def fill_patient_info_db():
    patient_info = ""
    for i in db_user_data_format:
        patient_info = patient_info+ i+ " text,"
    return patient_info[0:-1]


