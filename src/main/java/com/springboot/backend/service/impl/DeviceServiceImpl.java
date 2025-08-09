package com.springboot.backend.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.springboot.backend.MQTT.MqttServerConfig;
import com.springboot.backend.model.*;
import com.springboot.backend.repository.*;
import com.springboot.backend.request.AddDeviceRequest;
import com.springboot.backend.request.AddMasterDeviceRequest;
import com.springboot.backend.request.UpdateDeviceRequest;
import com.springboot.backend.service.DeviceService;
import com.springboot.backend.service.TopicService;
import jakarta.persistence.EntityNotFoundException;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static com.springboot.backend.MQTT.MqttSubscriber.subscribeToTopic;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private MqttClient mqttClient;
    @Autowired
    private DeviceTypeRepository deviceTypeRepository;
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceSubTypeRepository deviceSubTypeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private DataResponseRepository dataResponseRepository;
    @Autowired
    private SensorDataRepository sensorDataRepository;

    @Autowired
    private MasterDeviceRepo masterDeviceRepo;

    @Autowired
    private MqttServerConfigRepository mqttServerConfigRepository;

    @Autowired
    private EncryptionSettingsRepository encryptionSettingsRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository, MqttServerConfigRepository mqttServerConfigRepository, TopicRepository topicRepository, EncryptionSettingsRepository encryptionSettingsRepository, MqttClient mqttClient, FieldRepository fieldRepository, DataResponseRepository dataResponseRepository, SensorDataRepository sensorDataRepository, MasterDeviceRepo masterDeviceRepo) {
        super();
        this.deviceRepository = deviceRepository;
        this.fieldRepository = fieldRepository;
        this.dataResponseRepository = dataResponseRepository;
        this.sensorDataRepository = sensorDataRepository;
        this.masterDeviceRepo = masterDeviceRepo;
        this.mqttClient = mqttClient;
        this.mqttServerConfigRepository = mqttServerConfigRepository;
        this.encryptionSettingsRepository = encryptionSettingsRepository;
        this.topicRepository = topicRepository;
    }

    public boolean isMqttClientConnected() {
        IMqttClient mqttClient;
        try {
            mqttClient = new MqttClient("tcp://mqtt.server.com:1883", MqttClient.generateClientId());
            return mqttClient.isConnected();
        } catch (MqttException e) {
            // Handle the exception appropriately
            return false;
        }
    }

    @Override
    public Device addDevice(AddDeviceRequest addDeviceRequest) {
        User user = userRepository.getUserById(addDeviceRequest.getUser_id());
        DeviceTypes deviceTypes = deviceTypeRepository.findDeviceTypesById(addDeviceRequest.getDevice_type_id());
        DeviceSubType deviceSubType = deviceSubTypeRepository.findDeviceSubTypeById(addDeviceRequest.getDevice_subtype_id());
        Device addDevice = new Device();

        addDevice.setDevice_name(addDeviceRequest.getDevice_name());
        addDevice.setDevice_uuid(addDeviceRequest.getDevice_uuid());
        addDevice.setDevice_serial_number(addDeviceRequest.getDevice_serial_no());
        addDevice.setMac_address(addDeviceRequest.getDevice_mac_address());
        addDevice.setUser(user);
        addDevice.setDeviceTypes(deviceTypes);
        addDevice.setDeviceSubType(deviceSubType);
        // Check if the device already exists in the database
        Device existingDevice = deviceRepository.findDeviceById(addDevice.getId());
        if (existingDevice != null) {
            // Handle the scenario where the device already exists, such as throwing an exception or updating the existing device
            throw new RuntimeException("Device already exists");
        }
        // Check if the device already exists
        if (deviceExistsInFile(addDevice.getId())) {
            throw new RuntimeException("Device already exists");
            // Alternatively, you can handle the existing device scenario differently
            // e.g., return null, update the existing device, etc.
        }
        Device savedDevice = deviceRepository.save(addDevice);
        addDevice.setId(savedDevice.getId());
        Field field = fieldRepository.findByUserId(user.getId());
        addDevice.setControl_topic(field.getId() + "/" +savedDevice.getDevice_uuid() + "/" + "control");
        addDevice.setData_topic(field.getId() + "/" +savedDevice.getDevice_uuid() + "/" + "data");
        addDevice.setNotification_topic(field.getId() + "/" +savedDevice.getDevice_uuid() + "/" + "notification");
        addDevice.setDeviceState_topic(field.getId() + "/" +savedDevice.getDevice_uuid() + "/" + "deviceState");
        // addDevice.setConfigurationReady_topic(savedDevice.getDevice_uuid() + "/" + "configurationReady");

        // Save the updated device information
        Device updatedDevice = deviceRepository.save(addDevice);

        // Save the device info to a JSON file
        saveDeviceInfoToFile(updatedDevice);

        return updatedDevice;
    }

    @Override
    public MasterDevice addMaster(AddMasterDeviceRequest request) {
        DeviceTypes deviceType = deviceTypeRepository.findDeviceTypesById(request.getDeviceType());
        DeviceSubType deviceSubType = deviceSubTypeRepository.findDeviceSubTypeById(request.getDeviceSubType());
        Field field = fieldRepository.findFieldById(request.getField());
        User user = userRepository.getUserById(request.getUser());

        MasterDevice addDevice = new MasterDevice();
        addDevice.setDevice_name(request.getDeviceName());
        addDevice.setCommand("configurationReadyRequest");
        addDevice.setDevice_uuid(Long.parseLong(request.getDeviceUuid()));
        addDevice.setDevice_serial_number(request.getDeviceSerialNumber());
        addDevice.setMac_address(request.getMacAddress());
        addDevice.setUser(user);
        addDevice.setDevice_type_id(request.getDeviceType());
        addDevice.setDevice_sub_type_id(request.getDeviceSubType());
        addDevice.setDevice_status(0);
        addDevice.setDelete_status(0);
        addDevice.setField_id(field.getId());
        // addDevice.setEncryptionKeys(request.getEncryptionSettings());

        // Check if the device already exists in the database
        MasterDevice existingDevice = masterDeviceRepo.findMasterDeviceByUUID(request.getDeviceUuid());
        if (existingDevice != null) {
            throw new RuntimeException("Device already exists");
        }

        // Check if the device already exists in the file
        if (deviceExistsInFile(addDevice.getId())) {
            throw new RuntimeException("Device already exists");
        }

        MasterDevice savedDevice = masterDeviceRepo.save(addDevice);
        // Generate topics for the device
        TopicService topicService = new TopicService();
        Topics topics = topicService.generateTopics(savedDevice.getField_id(), String.valueOf(savedDevice.getDevice_uuid()));

        // Set the generated topics to the saved device
        topics.setControlTopic(topics.getControlTopic());
        topics.setDataTopic(topics.getDataTopic());
        topics.setNotificationTopic(topics.getNotificationTopic());
        topics.setDeviceStateTopic(topics.getDeviceStateTopic());
        topics.setConfigurationReadyTopic(topics.getConfigurationReadyTopic());
        topics.setIrrigationOverrideTopic(topics.getIrrigationOverrideTopic());
        topics.setMasterDevice(savedDevice);
        topics = topicRepository.save(topics);
        savedDevice.setTopics(topics);

        savedDevice.setAttachedDevices(request.getAttachedDevices());
        savedDevice.setAttachedAreas(request.getAttachedAreas());
        savedDevice.setAttachedPatches(request.getAttachedPatches());
        //savedDevice.setTopics();

        MqttServerConfig mqttConfig = new MqttServerConfig();
        mqttConfig.setMqttServerConfigUrl("http://localhost:8080/echoPost");
        mqttConfig.setMqttServerUrl("tcp://localhost:1883");
        mqttConfig.setMqttUsername("emqx_test");
        mqttConfig.setMqttPassword("emqx_test_password");
        mqttConfig.setMqttClientId("12345");
        mqttConfig.setMasterDevice(savedDevice);
        mqttConfig = mqttServerConfigRepository.save(mqttConfig);
        savedDevice.setMqttServerConfig(mqttConfig);


        EncryptionSettings encryptionSettings = new EncryptionSettings();
        encryptionSettings.setEncryptionKey("MTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTI=".getBytes());
        encryptionSettings.setIV1("MTIzNDU2Nzg5MDEyMzQ1Ng==".getBytes());
        encryptionSettings.setIV2("YWJjZGVmZ2hpamtsbW5vcA==".getBytes());

      /*  String encodedKey = Base64.getEncoder().encodeToString(key);
        String encodedIV1 = Base64.getEncoder().encodeToString(iv1);
        String encodedIV2 = Base64.getEncoder().encodeToString(iv2);*/
/*
        encryptionSettings.setEncryptionKey("123456789".getBytes());
        encryptionSettings.setIV1("1234567890123456".getBytes());
        encryptionSettings.setIV2("1234567890123456".getBytes());*/
        encryptionSettings.setMasterDevice(savedDevice);

        encryptionSettings = encryptionSettingsRepository.save(encryptionSettings);

        if (savedDevice != null && encryptionSettings != null) {
            savedDevice.setEncryptionSettings(encryptionSettings);
            //  MasterDevice updatedDevice = masterDeviceRepo.save(savedDevice);
            // Handle the updatedDevice object as needed
        }

        // Save the updated device information
        MasterDevice updatedDevice = masterDeviceRepo.save(savedDevice);

        // Save the device info to a JSON file
        saveMasterDeviceInfoToFile(updatedDevice);

        return updatedDevice;
    }


    @Override
    public Device attachDevice(long fieldId, long deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new EntityNotFoundException("Device not found with ID: " + deviceId));

        device.setField(device.getField());
        String controlTopic = fieldId + "/" + device.getDevice_uuid() + "/control";
        String dataTopic = fieldId + "/" + device.getDevice_uuid() + "/data";
        String notificationTopic = fieldId + "/" + device.getDevice_uuid() + "/notification";
        String deviceStateTopic = fieldId + "/" + device.getDevice_uuid() + "/deviceState";
        //String configurationReadyTopic = fieldId + "/" + device.getDevice_uuid() + "/configurationReady";

        device.setControl_topic(controlTopic);
        device.setData_topic(dataTopic);
        device.setNotification_topic(notificationTopic);
        device.setDeviceState_topic(deviceStateTopic);
        // device.setConfigurationReady_topic(configurationReadyTopic);

        Device updatedDevice = deviceRepository.save(device);
        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new EntityNotFoundException("Field not found with ID: " + fieldId));

        updatedDevice.setField(field);
        field.setDevice(device);
        fieldRepository.save(field);
        // Save updated device info to a JSON file
        saveDeviceInfoToFile(updatedDevice);

        // Subscribe to topics
        subscribeToTopic(controlTopic);
        subscribeToTopic(dataTopic);
        subscribeToTopic(notificationTopic);
        subscribeToTopic(deviceStateTopic);
        //  subscribeToTopic(configurationReadyTopic);
        System.out.println("Device subscribed to topics : " + "\n" + controlTopic + "\n" + dataTopic + "\n" + notificationTopic + "\n" + deviceStateTopic + "\n");

        return updatedDevice;
    }

    private boolean topicExists(String topic) {
        // Implement the logic to check if the topic already exists
        // You can use your own approach based on your requirements
        // For example, you can check if the topic exists in a database or in a list of existing topics
        // Return true if the topic exists, otherwise false
        // Example implementation:
        // return topicRepository.existsByTopic(topic);
        return false;
    }

    @Override
    public Device detachDevice(long deviceID) {
        Device device = deviceRepository.findById(deviceID)
                .orElseThrow(() -> new EntityNotFoundException("Device not found with ID: " + deviceID));

        device.setField(device.getField());
        detachFieldIdFromTopic(device, "/control");
        detachFieldIdFromTopic(device, "/data");
        detachFieldIdFromTopic(device, "/notification");
        detachFieldIdFromTopic(device, "/deviceState");
        detachFieldIdFromTopic(device, "/configurationReady");

        Device updatedDevice = deviceRepository.save(device);
        // Save updated device info to a JSON file
        saveDeviceInfoToFile(updatedDevice);

        return updatedDevice;
    }

    @Override
    public Device findDeviceByID(long deviceID) {
        return deviceRepository.findDeviceById(deviceID);
    }

    private void detachFieldIdFromTopic(Device device, String topicSuffix) {
        String topic = device.getField().getId() + "/" + device.getId() + topicSuffix;
        String updatedTopic = topic.replace(String.valueOf(device.getField().getId()), "");
        if (!updatedTopic.startsWith("/")) {
            updatedTopic = "/" + updatedTopic;
        }
        if (updatedTopic.endsWith("/")) {
            updatedTopic = updatedTopic.substring(0, updatedTopic.length() - 1);
        }
        updatedTopic = updatedTopic.substring(1).trim(); // Remove the leading '/' character

        switch (topicSuffix) {
            case "/control" -> device.setControl_topic(updatedTopic);
            case "/data" -> device.setData_topic(updatedTopic);
            case "/notification" -> device.setNotification_topic(updatedTopic);
            case "/deviceState" -> device.setDeviceState_topic(updatedTopic);
            //  case "/configurationReady" -> device.setConfigurationReady_topic(updatedTopic);
            default -> {
            }
            // Handle other topic suffixes if needed
        }
    }


    // Check if device exists in the text file
    private boolean deviceExistsInFile(long deviceId) {
        try {
            String filePath = "deviceInfo.json";
            File file = new File(filePath);
            List<Device> devices = readDeviceListFromFile(file);
            return devices.stream().anyMatch(d -> d.getId() == deviceId);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Read the list of devices from the text file
    private List<Device> readDeviceListFromFile(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Device> devices = new ArrayList<>();
        if (file.exists()) {
            byte[] fileData = Files.readAllBytes(file.toPath());
            devices = objectMapper.readValue(fileData, new TypeReference<List<Device>>() {
            });
        }
        return devices;
    }

    // Save device info to file
    private void saveDeviceInfoToFile(Device device) {
        try {
            String fileName = device.getDevice_name() + ".json";
          //  String filePath = "src/main/resources/" + fileName;
            File file = new File(fileName);

            // Convert device to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            String deviceJson = objectMapper.writeValueAsString(device);

            // Write JSON data to file
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(deviceJson);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMasterDeviceInfoToFile(MasterDevice device) {
        try {
            String fileName = device.getDevice_name() + ".json";
           // String filePath = "src/main/resources/" + fileName;
            File file = new File(fileName);

            // Convert device to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String deviceJson = objectMapper.writeValueAsString(device);

            // Write JSON data to file
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(deviceJson);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void publishToTopic(String topic, String message) {
        try {
            mqttClient.publish(topic, message.getBytes(), 0, false);
        } catch (Exception e) {
            // Handle the exception appropriately
        }
    }


    /* public void subscribeToTopicsOnStartup() {
         // Get the list of devices
         List<Device> devices = deviceRepository.findAll(); // Modify this based on your data access approach

         // Iterate through the devices and attach them to their respective field and subscribe to topics
         for (Device device : devices) {
             long fieldId = device.getFieldID();
             long deviceId = device.getId();

             attachDevice(fieldId, deviceId);
         }
     }
 */
    @Override
    public List<Device> getDevices() {
        return deviceRepository.findAll();
    }

    @Override
    public Device updateDevice(UpdateDeviceRequest updateDeviceRequest, long id) {
        Device deviceExists = deviceRepository.findDeviceById(id);
        deviceExists.setDevice_name(updateDeviceRequest.getDevice_name());

        return deviceRepository.save(deviceExists);
    }

}
