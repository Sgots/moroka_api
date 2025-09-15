package com.springboot.backend.controller;

import com.springboot.backend.model.Area;
import com.springboot.backend.model.Device;
import com.springboot.backend.model.MasterDevice;
import com.springboot.backend.model.SensorData;
import com.springboot.backend.repository.DeviceRepository;
import com.springboot.backend.request.AddDeviceRequest;
import com.springboot.backend.request.AddMasterDeviceRequest;
import com.springboot.backend.request.UpdateDeviceRequest;
import com.springboot.backend.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        super();
        this.deviceService = deviceService;
    }


    @PostMapping("/add-device")
    public Device addDevice(@Valid @RequestBody AddDeviceRequest addDeviceRequest)   {
        return deviceService.addDevice(addDeviceRequest);
    }

    @PostMapping("/add-master")
    public ResponseEntity<MasterDevice> addMasterDevice(@RequestBody AddMasterDeviceRequest request) {
        MasterDevice addedDevice = deviceService.addMaster(request);
        return ResponseEntity.ok(addedDevice);
    }

    // src/main/java/com/springboot/backend/controller/DeviceController.java
    @PutMapping("/attach-device/{fieldId}/{areaId}/{patchId}/{deviceId}")
    public ResponseEntity<Device> attachDeviceToPatch(
            @PathVariable long fieldId,
            @PathVariable int areaId,
            @PathVariable long patchId,
            @PathVariable long deviceId) {
        Device d = deviceService.attachDevice(fieldId, areaId, patchId, deviceId);
        return ResponseEntity.ok(d);
    }


    // Endpoint to detach the fieldID from a device
    @PutMapping("detach-device/{deviceID}")
    public ResponseEntity<Device> detachFieldIDFromDevice(@PathVariable("deviceID") long deviceID) {
        Device detachedDevice = deviceService.detachDevice(deviceID);
        return ResponseEntity.ok(detachedDevice);
    }

    @GetMapping("/sensor-data/{fieldId}/{userId}/{deviceUuid}/{layerLevel}")
    public List<Device> getDevicesWithSensorData(
            @PathVariable("fieldId") Long fieldId,
            @PathVariable("userId") Long userId,
            @PathVariable("deviceUuid") String deviceUuid,
            @PathVariable("layerLevel") Integer layerLevel) {
        return deviceRepository.findDevicesWithSensorData(fieldId, userId, deviceUuid, layerLevel);
    }


    @GetMapping("/field-devices/{fieldId}/{userId}")
    public List<Device> getDevicesByFieldIdAndUserId(
            @PathVariable("fieldId") Long fieldId,
            @PathVariable("userId") Long userId) {
        return deviceRepository.findDevicesByFieldIdAndUserId(fieldId, userId);
    }

    @PutMapping("/update-device/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable ("id") long deviceID,
                                                     @RequestBody UpdateDeviceRequest updateDeviceRequest){
        return new ResponseEntity<Device>(deviceService.updateDevice(updateDeviceRequest, deviceID), HttpStatus.OK);

    }

    @PostMapping("/delete-device/{id}")
    public ResponseEntity<String> deleteDevice(@PathVariable("id") long deviceID){
        deviceRepository.markDeviceAsDeleted(deviceID);
        return new ResponseEntity<String>("Device Deleted", HttpStatus.OK);
    }

    @GetMapping("/user-devices")
    public List<Device> getAllDevices(){
        return deviceService.getDevices();
    }
    @GetMapping("/user-devices/{id}")
    public List<Device> getUserDevices(@PathVariable("id") long userID){
        return deviceRepository.findAllByUserId(userID);
    }

    @GetMapping("/getDevice/{device_id}")
    public ResponseEntity<Device> getDeviceByID(@PathVariable("device_id") long deviceID){
        return new ResponseEntity<Device>(deviceService.findDeviceByID(deviceID),HttpStatus.OK);
    }

    @GetMapping("/get-device/{userId}")
    public ResponseEntity<Device> getLatestDeviceByUserId(@PathVariable Long userId) {
        Device device = deviceRepository.findLatestDeviceByUserId(userId);
        if (device != null) {
            return ResponseEntity.ok(device);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/area-devices/{areaId}/{userId}")
    public ResponseEntity<List<Device>> getAreaDevices(
            @PathVariable int areaId,
            @PathVariable long userId) {
        List<Device> devices = deviceService.getAreaDevices(areaId, userId);
        return ResponseEntity.ok(devices);
    }
    @GetMapping("/fetchPatchDevice/{patchId}/{userId}")
    public ResponseEntity<List<Device>> fetchPatchDevice(
            @PathVariable long patchId,
            @PathVariable long userId) {
        List<Device> devices = deviceService.getPatchDevices(patchId, userId);
        return ResponseEntity.ok(devices);
    }
}
