package com.springboot.backend.repository;

import com.springboot.backend.model.FlowMeter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowMeterRepository extends JpaRepository<FlowMeter, Long> {

    @Query(value="select * from tbl_flow_meter d where d.device_id = :id order by id desc limit 1", nativeQuery=true)
    List<FlowMeter> getDeviceFlowMeterResponse(long id);


    @Query(value="select * from tbl_flow_meter\n" +
            "    join tbl_devices on tbl_devices.device_uuid = tbl_flow_meter.device_id\n" +
            "    join tbl_users tu on tbl_devices.user_id = tu.user_id= :user_id order by tbl_flow_meter.id desc limit 1", nativeQuery=true)
    FlowMeter getDispensedWater(long user_id);
}
