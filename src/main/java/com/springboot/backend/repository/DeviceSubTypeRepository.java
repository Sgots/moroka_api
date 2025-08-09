package com.springboot.backend.repository;

import com.springboot.backend.model.DeviceSubType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceSubTypeRepository extends JpaRepository<DeviceSubType, Long> {

    DeviceSubType findDeviceSubTypeById(long id);
}
