package com.springboot.backend.service;

import com.springboot.backend.model.Device;
import com.springboot.backend.model.MasterDevice;
import com.springboot.backend.request.AddDeviceRequest;
import com.springboot.backend.request.AddMasterDeviceRequest;
import com.springboot.backend.request.UpdateDeviceRequest;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface DeviceService {
    Device addDevice(AddDeviceRequest addDeviceTypeRequest);

    MasterDevice addMaster(AddMasterDeviceRequest request);

    // src/main/java/com/springboot/backend/service/DeviceService.java
    Device attachDevice(long fieldId, int areaId,long patchId, long deviceId);
    List<Device> getAreaDevices(int areaId, long userId);
    List<Device> getPatchDevices(long patchId, long userId);



    Device detachDevice(long device);

    Device findDeviceByID(long deviceID);

    List<Device> getDevices();

    Device updateDevice(UpdateDeviceRequest updateDeviceRequest, long id);
}
