import paho.mqtt.client as mqtt
import random, threading, json
from datetime import datetime
import random
from collections import OrderedDict 

#MQTT_Broker = "ec2-13-126-236-156.ap-south-1.compute.amazonaws.com"
MQTT_Broker = "localhost"
MQTT_Port = 1883
MQTT_Topic = "dementia_care"
Keep_Alive_Interval = 45

#====================================================

def on_connect(client, udata, rc):
    if rc != 0:
        print "Unable to connect to MQTT Broker..."
    else:
        print "Connected with MQTT Broker: " + str(MQTT_Broker)

def on_publish(client, userdata, mid):
    pass

def on_disconnect(client, userdata, rc):
    if rc !=0:
        pass

mqttc = mqtt.Client()
mqttc.on_connect = on_connect
mqttc.on_disconnect = on_disconnect
mqttc.on_publish = on_publish
mqttc.connect(MQTT_Broker, int(MQTT_Port), int(Keep_Alive_Interval))

def publish_topic(topic, message):
    mqttc.publish(topic,message)
    print ("Published: " + str(message) + " " + "on MQTT Topic: " + str(topic))
    print ""

#====================================================
# SENSOR  data to MQTT Broker

def random_select(l):
    idx = random.randint(0, len(l)-1)
    return l[idx]

#hospital_list = ["cosmo", "PRS", "Narayana", "Lotus"]
patient_list  = ["goutham", "kamal", "dhanya", "suma", "kannam"]
#patient_id_list  = ["1001", "1003", "1005", "1007", "1009"]
patient_id_list  = ["1001"]
heart_beat = ["40", "50", "60", "70", "80", "90"]
step_count = 100
rssi = ["50", "40", "60", "80", "90", "85"]
fall_detection_list = ["0", "1"]
reader_list = ["reader1", "reader2"]

def publish_fake_sensor_values_to_mqtt():
    global step_count
    threading.Timer(3.0, publish_fake_sensor_values_to_mqtt).start()
    sensor_data = OrderedDict()
    sensor_data['Date_n_Time'] = (datetime.today()).strftime("%d-%b-%Y %H:%M:%S:%f")
    sensor_data['HospitalId'] = "cosmohospital" 
    sensor_data['ReaderId']   = random_select(reader_list)
    sensor_data['Patient_Id']   = random_select(patient_id_list)
    sensor_data['Calorie']   = "40"
    sensor_data['HeartBeat']   = random_select(heart_beat)
    sensor_data['StepCount']   = step_count+25
    step_count += 25
    sensor_data['Bat_VTG']   = "3.3"
    sensor_data['rssi']   = random_select(rssi)
    sensor_data['FallDetection']   = random_select(fall_detection_list)
    val = ""
    for v in sensor_data.values():
        val = val+str(v) +'|'
    print(val[0:-1])

    publish_topic(MQTT_Topic, val)

def publish_fake_sensor_values_to_mqtt_one_time():
    global step_count
    #threading.Timer(3.0, publish_fake_sensor_values_to_mqtt).start()
    sensor_data = OrderedDict()
    sensor_data['HospitalId'] = "cosmohospital" 
    sensor_data['ReaderId']   = "reader1"
    sensor_data['Date_n_Time'] = (datetime.today()).strftime("%d-%b-%Y %H:%M:%S:%f")
    #sensor_data['ReaderId']   = random_select(reader_list)
    sensor_data['HeartBeat']   = random_select(heart_beat)
    sensor_data['Patient_Id']   = 1001
    sensor_data['StepCount']   = step_count+25
    step_count += 25
    sensor_data['Calorie']   = "150"
    sensor_data['Temparature']   = "33"
    sensor_data['FallDetection']   = "1"
    sensor_data['Bat_VTG']   = "2"
    sensor_data['RSSI']   = "40"
    #sensor_data['rssi']   = random_select(rssi)
    val_1 = ""
    for v in sensor_data.values():
        val_1 = val_1+str(v) +'|'
    print(val_1[0:-1])

    publish_topic(MQTT_Topic, val_1[0:-1])

#publish_fake_sensor_values_to_mqtt()
publish_fake_sensor_values_to_mqtt_one_time()
#publish_fake_sensor_values_to_mqtt_one_time()



#====================================================
