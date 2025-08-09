package com.springboot.backend.MQTT;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttClientConfig {
    @Value("${mqtt.broker}")
    private String brokerUrl;

    @Value("${mqtt.clientId}")
    private String clientId;



    @Bean
    public MqttClient mqttClient() throws Exception {
        MqttConnectOptions options = new MqttConnectOptions();


        MqttClient mqttClient = new MqttClient(brokerUrl, clientId);
        mqttClient.connect(options);

        return mqttClient;
    }
}
