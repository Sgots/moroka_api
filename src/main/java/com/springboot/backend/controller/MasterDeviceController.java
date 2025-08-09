package com.springboot.backend.controller;

import com.springboot.backend.model.MasterDevice;
import com.springboot.backend.request.AddMasterDeviceRequest;
import com.springboot.backend.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MasterDeviceController {

    @Autowired
    private DeviceService deviceService;

 /*   @PostMapping("/add-master")
    public ResponseEntity<MasterDevice> addMasterDevice(@RequestBody AddMasterDeviceRequest request) {
        MasterDevice addedDevice = deviceService.addMaster(request);
        return ResponseEntity.ok(addedDevice);
    }*/
}