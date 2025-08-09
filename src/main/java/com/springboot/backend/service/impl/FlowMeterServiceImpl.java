package com.springboot.backend.service.impl;

import com.springboot.backend.model.FlowMeter;
import com.springboot.backend.repository.*;
import com.springboot.backend.service.FlowMeterService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlowMeterServiceImpl implements FlowMeterService {

    @Autowired
    private FlowMeterRepository flowMeterRepository;


    public FlowMeterServiceImpl(FlowMeterRepository flowMeterRepository) {
        super();
        this.flowMeterRepository = flowMeterRepository;

    }

  /*  @Override
    public List <FlowMeter> findByDeviceID(long deviceID) {
        return flowMeterService.findByDeviceID(deviceID);
    }*/

    @Override
    public List<FlowMeter> getFlowMeter() {
        return null;
    }
}
