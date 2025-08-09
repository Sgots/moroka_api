package com.springboot.backend.MQTT;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.springboot.backend.model.Area;
import com.springboot.backend.model.Device;
import com.springboot.backend.repository.AreaRepository;
import com.springboot.backend.repository.DataResponseRepository;
import com.springboot.backend.request.StateControlRequest;
import com.springboot.backend.request.UpdateAreaRequest;
import com.springboot.backend.request.UpdateDeviceRequest;
import com.springboot.backend.service.AreaService;
import jakarta.validation.Valid;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class MqttController {

    @Autowired
    private MqttPublisher mqttPublisher;
    @Autowired
    private DataResponseRepository dataResponseRepository;

    public MqttController(MqttPublisher mqttPublisher, DataResponseRepository dataResponseRepository) {
        super();
        this.mqttPublisher = mqttPublisher;
        this.dataResponseRepository= dataResponseRepository;
    }

    @PostMapping("/controlState1")
    public ResponseEntity<String> publishControlState1(@RequestBody StateControlRequest request) {
        try {
            String deviceID = request.getDeviceID();
            int fieldID = request.getFieldID();

            // Call the publishControlState1 method with the provided deviceID and fieldID
            mqttPublisher.publishControlState1(fieldID, deviceID);

            return ResponseEntity.ok("Message published successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error publishing message: " + e.getMessage());
        }
    }
    @PostMapping("/controlState0")
    public ResponseEntity<String> publishControlState0(@RequestBody StateControlRequest request) {
        try {
            String deviceID = request.getDeviceID();
            int fieldID = request.getFieldID();

            // Call the publishControlState1 method with the provided deviceID and fieldID
            mqttPublisher.publishControlState0(fieldID, deviceID);

            return ResponseEntity.ok("Message published successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error publishing message: " + e.getMessage());
        }
    }

}
