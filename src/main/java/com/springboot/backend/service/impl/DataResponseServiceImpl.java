package com.springboot.backend.service.impl;

import com.springboot.backend.model.DataResponse;
import com.springboot.backend.repository.DataResponseRepository;
import com.springboot.backend.service.DataResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataResponseServiceImpl implements DataResponseService {
    @Autowired
    private DataResponseRepository dataResponseRepository;
   /* @Override
    public DataResponse getDeviceByIDFromResponse(long id) {
        return dataResponseRepository.getDeviceByIDFromResponse(id);
    }*/

    @Override
    public DataResponse getDeviceByIDFromResponse(long id) {
        return null;
    }

    /* @Override
        public List<DataResponse> getFieldDevice(long id) {
            return dataResponseRepository.getFieldDevice( id);
        }
    */
    @Override
    public List<DataResponse> getDevicesResponse(long id) {
        return dataResponseRepository.getDevicesResponse( id);
    }

 /*   @Override
    public List<DataResponse> getDeviceResponse() {
        return dataResponseRepository.getDeviceResponse( );
    }
*/
/*    @Override
    public List<DataResponse> getAllById(long id) {
        return dataResponseRepository.findAllByDeviceID(id);
    }*/

}
