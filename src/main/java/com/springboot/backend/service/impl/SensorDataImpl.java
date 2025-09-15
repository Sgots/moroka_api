package com.springboot.backend.service.impl;

import com.springboot.backend.model.SensorData;
import com.springboot.backend.repository.SensorDataRepository;
import com.springboot.backend.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorDataImpl implements SensorDataService {

    @Autowired
    private SensorDataRepository sensorDataRepo;

    @Override
    public List<SensorData> getAllById(long id, long layer_level) {
        return null;
    }

    @Override
    public List<SensorData> findAllByDeviceID(long id) {
        return sensorDataRepo.findAllByDeviceID(id);
    }

    @Override
    public List<SensorData> findBySensorDataByFieldID(Long fieldId, Long deviceId, Integer layerLevel, Long userId) {
        return sensorDataRepo.findBySensorDataByFieldID(fieldId, deviceId, layerLevel, userId);
    }

    public List<SensorData> getFieldData(Long fieldId, Long deviceId, Integer layerLevel,  Long userId) {
        return sensorDataRepo.findBySensorDataByFieldID(fieldId, deviceId, layerLevel, userId);
    }
}
