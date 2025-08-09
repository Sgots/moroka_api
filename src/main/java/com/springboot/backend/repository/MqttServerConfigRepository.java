package com.springboot.backend.repository;

import com.springboot.backend.MQTT.MqttServerConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MqttServerConfigRepository extends JpaRepository<MqttServerConfig, Long> {
    // Add custom repository methods if needed
}
