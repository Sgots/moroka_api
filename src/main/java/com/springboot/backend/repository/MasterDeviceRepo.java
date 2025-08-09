package com.springboot.backend.repository;

import com.springboot.backend.model.Device;
import com.springboot.backend.model.MasterDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface MasterDeviceRepo extends JpaRepository<MasterDevice, Long> {

    MasterDevice findMasterDeviceById(String id);

    @Query("from MasterDevice d where d.device_uuid= :uuid ")
    MasterDevice findMasterDeviceByUUID(String uuid);

    @Transactional
    @Modifying
    @Query("update MasterDevice set delete_status = 1 where id =:id")
    void markDeviceAsDeleted(@Param("id") Long id);
}
