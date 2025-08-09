package com.springboot.backend.MQTT;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.backend.model.MasterDevice;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_server_config")
public class MqttServerConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    String mqttServerConfigUrl = "http://localhost:8080/echoPost";
    String mqttServerUrl = "tcp://localhost:1883";
    String mqttUsername = "emqx_test";
    String mqttPassword = "emqx_test_password";
    String mqttClientId = "12345";
    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private MasterDevice masterDevice;
    // Constructors, getters, and setters
}
