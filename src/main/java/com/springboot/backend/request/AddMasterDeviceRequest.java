package com.springboot.backend.request;

import com.springboot.backend.MQTT.MqttServerConfig;
import com.springboot.backend.model.*;
import lombok.Data;

import java.util.List;

@Data
public class AddMasterDeviceRequest {

    private long user;
    private int deviceType;
    private int deviceSubType;
    private long field;
    private String deviceUuid;
    private String deviceSerialNumber;
    private String deviceName;
    private String command;
    private String macAddress;
    private EncryptionSettings encryptionSettings;
    private MqttServerConfig mqttServerConfig;
    private Topics topics;
    private List<Device> attachedDevices;
    private List<Area> attachedAreas;
    private List<Patch> attachedPatches;

    // Constructor, getters, and setters
}
