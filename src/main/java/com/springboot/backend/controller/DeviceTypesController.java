package com.springboot.backend.controller;

import com.springboot.backend.model.DeviceTypes;
import com.springboot.backend.model.Field;
import com.springboot.backend.repository.DeviceTypeRepository;
import com.springboot.backend.service.DeviceTypesService;
import com.springboot.backend.service.FieldService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class DeviceTypesController {

    @Autowired
    private DeviceTypeRepository deviceTypeRepository;
    @Autowired
    private DeviceTypesService deviceTypesService;

    public DeviceTypesController(DeviceTypesService deviceTypesService) {
        super();
        this.deviceTypesService = deviceTypesService;
    }

    //get devicy types REST API
    //http://localhost:8080/api/v1/get-device-types
 /*   @GetMapping("/get-device-types")
    public List<?> getDeviceTypes(){
        return deviceTypesService.getDeviceTypes();
    }*/

    @RequestMapping(value = "/get-device-type/{_id}", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceTypes> findDeviceTypesById(@PathVariable("_id") int deviceID){
        return new ResponseEntity<DeviceTypes>(deviceTypeRepository.findDeviceTypesById(deviceID), HttpStatus.OK);
    }


    @GetMapping("/get-device-subtype/{device_type_id}")
    public List<?> findDeviceTypeWithDeviceID(@Valid @RequestBody @PathVariable("device_type_id") long deviceSubTypeId){
        return deviceTypeRepository.findDeviceSubTypes(deviceSubTypeId);
    }
}
