package com.springboot.backend.service;

import com.springboot.backend.model.DeviceSubType;
import com.springboot.backend.model.DeviceTypes;
import com.springboot.backend.repository.DeviceSubTypeRepository;
import com.springboot.backend.repository.DeviceTypeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DeviceSubTypeService {
    @Autowired
    final DeviceSubTypeRepository deviceSubTypeRepository;

    public DeviceSubTypeService(DeviceSubTypeRepository deviceSubTypeRepository) {
        this.deviceSubTypeRepository = deviceSubTypeRepository;
    }

    //Save topics to database
   /* @PostConstruct
    public void savePredefinedDeviceSubTypes() {
        List<String> predefinedSubTypeNames = Arrays.asList("Customer Interface Unit *Master", "Customer Interface Unit *Repeater", "Soil Sensor", "Weather Station");
        List<Integer> deviceTypeIds = Arrays.asList(1, 2, 3, 4); // Assuming the corresponding DeviceTypeIDs

        for (int i = 0; i < predefinedSubTypeNames.size(); i++) {
            String deviceTypeName = predefinedSubTypeNames.get(i);
            Integer deviceTypeIdValue = deviceTypeIds.get(i);

            DeviceTypes deviceType = new DeviceTypes();
            deviceType.setId(deviceTypeIdValue); // Assuming DeviceTypes has an 'id' field of type Integer
            // Set other fields of the DeviceTypes entity if necessary

            DeviceSubType devicesubTypes = new DeviceSubType();
            devicesubTypes.setDevice_subtype_name(deviceTypeName);
            devicesubTypes.setDevice_type(deviceType);
            devicesubTypes.setDelete_status(0);
            deviceSubTypeRepository.save(devicesubTypes);
        }
    }
*/


}
