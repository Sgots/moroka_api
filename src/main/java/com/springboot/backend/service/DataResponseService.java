package com.springboot.backend.service;

import com.springboot.backend.model.DataResponse;
import com.springboot.backend.model.Field;
import com.springboot.backend.repository.DataResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DataResponseService {

    DataResponse getDeviceByIDFromResponse(long id);
   // List <DataResponse> getFieldDevice(long id);
    List<DataResponse> getDevicesResponse(long id);
   // List<DataResponse> getDeviceResponse();

    /*    List<DataResponse> getAllById(long id);*/

}
