package com.springboot.backend.controller;

import com.springboot.backend.model.DataResponse;
import com.springboot.backend.model.Device;
import com.springboot.backend.repository.DataResponseRepository;
import com.springboot.backend.service.DataResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class DataResponseController {

    @Autowired
    private DataResponseRepository dataResponseRepository;
    @Autowired
    private DataResponseService dataResponseService;

    public DataResponseController(DataResponseService dataResponseService) {
        super();
        this.dataResponseService = dataResponseService;
    }

    @RequestMapping(value = "/deviceResponse/{id}", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public List<DataResponse> getDeviceByID(@PathVariable("id") long deviceID){
        return dataResponseRepository.getDevicesResponse(deviceID);
    }

    @RequestMapping(value = "/viewDevice/{id}", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public List<DataResponse> viewDevice(@PathVariable("id") long deviceID){
        return dataResponseRepository.getDevicesResponse(deviceID);
    }

    @RequestMapping(value = "/device-response/{id}", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public DataResponse deviceResponseByID(@PathVariable("id") long deviceID){
        return dataResponseRepository.findLatestIdByDeviceId(deviceID);
    }

    @RequestMapping(value = "/field-response/{id}", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public DataResponse fieldResponseByID(@PathVariable("id") long deviceID){
        return dataResponseRepository.findFieldByID(deviceID);
    }
}
