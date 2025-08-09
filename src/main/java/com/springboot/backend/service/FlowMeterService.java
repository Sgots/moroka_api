package com.springboot.backend.service;

import com.springboot.backend.model.Device;
import com.springboot.backend.model.FlowMeter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FlowMeterService {
  //  List <FlowMeter> findByDeviceID(long deviceID);
    List<FlowMeter> getFlowMeter();

}
