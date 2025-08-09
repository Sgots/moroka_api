package com.springboot.backend.controller;

import com.springboot.backend.model.SensorData;
import com.springboot.backend.repository.SensorDataRepository;
import com.springboot.backend.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class SensorDataController {
    @Autowired
    private SensorDataRepository sensorDataRepo;
    @Autowired
    private SensorDataService sensorDataService;

    public SensorDataController(SensorDataService sensorDataService) {
        super();
        this.sensorDataService = sensorDataService;
    }

    @RequestMapping(value = "/sensorData/{id}/{layer_level}", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public List<SensorData> getSensorData(@PathVariable("id") long deviceID,@PathVariable("layer_level")  long layerLevel){
        return sensorDataRepo.getSensorData(deviceID,layerLevel);
    }
    @RequestMapping(value = "/sensorData/{id}", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public List<SensorData> getSensorDataByDeviceID(@PathVariable("id") long deviceID){
        return sensorDataRepo.findAllByDeviceID(deviceID);
    }

    @GetMapping("/{fieldId}/{deviceId}/{layerLevel}/{user_id}")
    public List<SensorData> getSensorData(
            @PathVariable Long fieldId,
            @PathVariable Long deviceId,
            @PathVariable Integer layerLevel,
            @PathVariable Long user_id) {
        return sensorDataService.findBySensorDataByFieldID(fieldId, deviceId, layerLevel, user_id);
    }

    @GetMapping("sensorData/{fieldId}/{userId}/{deviceUuid}/{layerLevel}")
    public List<SensorData> getFieldSensorData(@PathVariable("fieldId") Long fieldId,
                                          @PathVariable("userId") Long userId,
                                          @PathVariable("deviceUuid") String deviceUuid,
                                          @PathVariable("layerLevel") Integer layerLevel) {
        return sensorDataRepo.findSensorData(fieldId, userId, deviceUuid, layerLevel);
    }

  /*  @RequestMapping(value = "/sensorData/{field_id}/{device_id}/{layer_level}", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public List<SensorData> getFieldData(@PathVariable("field_id") long field_id,
                                         @PathVariable("device_id")  long device_id,
                                         @PathVariable("layer_level")  long layerLevel){
        return sensorDataRepo.getSensorData(field_id, device_id,layerLevel);
    }*/
}
