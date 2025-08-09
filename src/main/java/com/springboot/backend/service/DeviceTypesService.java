package com.springboot.backend.service;

import com.springboot.backend.model.DeviceSubType;
import com.springboot.backend.model.DeviceTypes;
import com.springboot.backend.repository.DeviceTypeRepository;
import com.springboot.backend.request.AddDeviceRequest;
import com.springboot.backend.request.UpdateDeviceTypeRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DeviceTypesService {

    @Autowired
     final DeviceTypeRepository deviceTypeRepository;

    public DeviceTypesService(DeviceTypeRepository deviceTypeRepository) {
        this.deviceTypeRepository = deviceTypeRepository;
    }

    //Save topics to database
    @PostConstruct
    public void savePredefinedDeviceTypes() {
        List<String> predefinedNames = Arrays.asList("Interfacers", "Sensors", "Actuators", "Meters");
        for (String deviceTypeName : predefinedNames) {
            if (!deviceTypeRepository.existsByName(deviceTypeName)) {
                DeviceTypes deviceType = new DeviceTypes();
                deviceType.setName(deviceTypeName);
                deviceType.setDelete_status(0);
                deviceTypeRepository.save(deviceType);
            }
        }
    }


}
