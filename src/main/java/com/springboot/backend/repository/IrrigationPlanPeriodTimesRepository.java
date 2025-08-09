package com.springboot.backend.repository;

import com.springboot.backend.model.IrrigationPlanPeriodTimes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IrrigationPlanPeriodTimesRepository extends JpaRepository<IrrigationPlanPeriodTimes, Long> {
    IrrigationPlanPeriodTimes findIrrigationPlanPeriodTimesById(long id);
}
