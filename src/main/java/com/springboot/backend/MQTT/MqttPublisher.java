package com.springboot.backend.MQTT;

import com.google.gson.Gson;
import com.springboot.backend.model.Field;
import com.springboot.backend.repository.FieldRepository;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class MqttPublisher {

    @Autowired
    private final MqttClient mqttClient;

    @Autowired
    private FieldRepository fieldRepository;

    public MqttPublisher(MqttClient mqttClient, FieldRepository fieldRepository) {
        this.mqttClient = mqttClient;
        this.fieldRepository = fieldRepository;
    }

    public void publishMessage(String topic, String message) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        MqttClient client = new MqttClient("tcp://localhost:1883", "12345");
        client.connect();
        client.publish(topic, mqttMessage);
    }


    public void publishControlState1(int fieldID, String deviceID) throws MqttException {
        MqttClient client = new MqttClient("tcp://localhost:1883", "12345");
        MqttMessage mqttMessage = new MqttMessage();
        client.connect();

        // Check if the client is connected before publishing
        if (client.isConnected()) {
            Field field = fieldRepository.findFieldById(fieldID);
            int state1 = 1;

            String controlState1topic = field.getId() + "/" + deviceID + "/control";
            //System.out.println("topic" + "\n" + controlState1topic);

            LinkedHashMap<String, Object> payload = new LinkedHashMap<>();
            payload.put("controlState", state1);
            payload.put("deviceID", String.valueOf(deviceID));
            payload.put("command", "deviceControlRequest");

            Gson gson = new Gson();
            String payloadString = gson.toJson(payload);

            int qos = 0;
            boolean retained = false;
            mqttMessage = new MqttMessage();
            mqttMessage.setPayload(payloadString.getBytes());
            mqttMessage.setQos(qos);
            mqttMessage.setRetained(retained);

            // Publish the message
            client.publish(controlState1topic, mqttMessage);
            System.out.println("message published" + "\n" + payloadString);
        } else {
            System.out.println("Failed to publish message. Client is not connected to the MQTT broker.");
        }

        // Disconnect from the MQTT broker
        client.disconnect();
    }


    public void publishControlState0(int fieldID, String deviceID) throws MqttException {
        MqttClient client = new MqttClient("tcp://localhost:1883", "12345");
        client.connect();
        MqttMessage mqttMessage = new MqttMessage();

        if (client.isConnected()) {
            Field field = fieldRepository.findFieldById(fieldID);
            int state0 = 0;

            String controlState1topic = field.getId() + "/" + deviceID + "/control";
            System.out.println("topic" + "\n" + controlState1topic);

            LinkedHashMap<String, Object> payload = new LinkedHashMap<>();
            payload.put("controlState", state0);
            payload.put("deviceID", String.valueOf(deviceID));
            payload.put("command", "deviceControlRequest");

            Gson gson = new Gson();
            String payloadString = gson.toJson(payload);

            int qos = 0;
            boolean retained = false;
            mqttMessage = new MqttMessage();
            mqttMessage.setPayload(payloadString.getBytes());
            mqttMessage.setQos(qos);
            mqttMessage.setRetained(retained);

            // Publish the message
            client.publish(controlState1topic, mqttMessage);
        } else {
            System.out.println("Failed to publish message. Client is not connected to the MQTT broker.");
        }

        // Disconnect from the MQTT broker
        client.disconnect();
    }
}

