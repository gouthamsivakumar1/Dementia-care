import paho.mqtt.client as mqtt
import random, threading, json
from datetime import datetime
import random

#MQTT_Broker = "ec2-13-126-236-156.ap-south-1.compute.amazonaws.com"
MQTT_Broker = "localhost"
MQTT_Port = 1883
MQTT_Topic = "dementia_care"
Keep_Alive_Interval = 45

#====================================================

def on_connect(client, userdata, rc):
    if rc != 0:
        pass
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
patient_id_list  = ["1001", "1003", "1005", "1007", "1009"]
heart_beat = ["40", "50", "60", "70", "80", "90"]
step_count = 100
rssi = ["40", "60", "80", "90", "85"]
fall_detection_list = ["0", "1"]
reader_list = ["reader0", "reader1"]

def publish_fake_sensor_values_to_mqtt():
    global step_count
    threading.Timer(3.0, publish_fake_sensor_values_to_mqtt).start()
    sensor_data = {}
    sensor_data['Date_n_Time'] = (datetime.today()).strftime("%d-%b-%Y %H:%M:%S:%f")
    sensor_data['HospitalId'] = "cosmo hospital" 
    sensor_data['ReaderId']   = random_select(reader_list)
    sensor_data['Patient_Id']   = random_select(patient_id_list)
    sensor_data['Calorie']   = "40"
    sensor_data['HeartBeat']   = random_select(heart_beat)
    sensor_data['StepCount']   = step_count+25
    step_count += 25
    sensor_data['Bat_VTG']   = "3.3"
    sensor_data['rssi']   = random_select(rssi)
    sensor_data['FallDetection']   = random_select(fall_detection_list)
    sensor_data_json = json.dumps(sensor_data)

    publish_topic(MQTT_Topic, sensor_data_json)

publish_fake_sensor_values_to_mqtt()

#====================================================
