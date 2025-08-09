package com.springboot.backend.repository;

import com.springboot.backend.model.Field;
import com.springboot.backend.model.IrrigationPlanPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface IrrigationPlanPeriodRepository extends JpaRepository <IrrigationPlanPeriod,Long> {
    //@Query("from IrrigationPlanPeriod where id =:id")
    IrrigationPlanPeriod findIrrigationPlanPeriodById(long id);

    @Transactional
    @Modifying
    @Query("update IrrigationPlanPeriod set delete_status = 1 where id =:id")
    void markIrrigationPlanPeriodAsDeleted(@Param("id") Long id);
}
