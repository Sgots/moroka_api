package com.springboot.backend.MQTT;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

@Component
public class MqttListener implements MqttCallback {

    @Override
    public void connectionLost(Throwable throwable) {
        // Handle connection loss
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // Handle incoming message
        System.out.println("Received message on topic: " + topic);
        System.out.println("Message: " + new String(message.getPayload()));
        // Save the message data to the database
        // ...
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Handle message delivery completion
    }
}
