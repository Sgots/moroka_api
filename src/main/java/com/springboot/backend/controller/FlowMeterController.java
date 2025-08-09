package com.springboot.backend.controller;

import com.springboot.backend.model.Device;
import com.springboot.backend.model.FlowMeter;
import com.springboot.backend.repository.DeviceRepository;
import com.springboot.backend.repository.FlowMeterRepository;
import com.springboot.backend.service.DeviceService;
import com.springboot.backend.service.FlowMeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class FlowMeterController {
    @Autowired
    private FlowMeterRepository flowMeterRepository;
    @Autowired
    private FlowMeterService flowMeterService;
    public FlowMeterController(FlowMeterService flowMeterService) {
        super();
        this.flowMeterService = flowMeterService;
    }
    @GetMapping("/get-meter/{device_id}")
    public List<FlowMeter> getFlowByDeviceID(@PathVariable("device_id") long deviceID){
        return flowMeterRepository.getDeviceFlowMeterResponse(deviceID);
    }

    @GetMapping("/get-dispensedWater/{id}")
    public FlowMeter getDispensedWater(@PathVariable("id") long uid){
        return flowMeterRepository.getDispensedWater(uid);
    }
}
