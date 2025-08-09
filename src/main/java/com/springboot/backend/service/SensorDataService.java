package com.springboot.backend.service;

import com.springboot.backend.model.DataResponse;
import com.springboot.backend.model.SensorData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SensorDataService {

    List<SensorData> getAllById(long id, long layer_level);

    List<SensorData> findAllByDeviceID(long device_id);

    List<SensorData> findBySensorDataByFieldID(Long fieldId, Long deviceId, Integer layerLevel,  Long userId);
}
