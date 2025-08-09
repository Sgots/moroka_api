package com.springboot.backend.repository;

import com.springboot.backend.model.Area;
import com.springboot.backend.model.DeviceSubType;
import com.springboot.backend.model.DeviceTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceTypeRepository extends JpaRepository <DeviceTypes, Long> {

    DeviceTypes findDeviceTypesById(long id);

    boolean existsByName(String deviceTypeName);

    @Query("FROM DeviceSubType d WHERE d.device_sub_type_id= :id and d.delete_status=0")
    List<DeviceSubType> findDeviceSubTypes(@Param("id") long id);
}
