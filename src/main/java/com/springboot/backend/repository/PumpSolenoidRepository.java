package com.springboot.backend.repository;

import com.springboot.backend.model.FlowMeter;
import com.springboot.backend.model.PumpSolenoid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PumpSolenoidRepository extends JpaRepository<PumpSolenoid, Long> {
    @Query(value="select * from tbl_pump_solenoid d where d.device_id = :id order by id desc limit 1", nativeQuery=true)
    List<FlowMeter> getSolenoidResponse(long id);
}
