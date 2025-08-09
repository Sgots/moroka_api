package com.springboot.backend.service;

import com.springboot.backend.model.IrrigationPlan;
import com.springboot.backend.model.IrrigationPlanPeriod;
import com.springboot.backend.request.AddIrrigationPlanRequest;
import com.springboot.backend.request.AddPlanPeriodRequest;
import com.springboot.backend.request.EditIrrigationPlanRequest;
import com.springboot.backend.request.UpdateIrrigationPlanPeriodRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IrrigationPlanPeriodService {
    IrrigationPlanPeriod addIrrigationPlanPeriod(AddPlanPeriodRequest addPlanPeriodRequest);
    List<IrrigationPlanPeriod> getIrrigationPlanPeriods();
    IrrigationPlanPeriod getIrrigationPlanPeriodByID(long id);
    IrrigationPlanPeriod updateIrrigationPlanPeriod(UpdateIrrigationPlanPeriodRequest updateIrrigationPlanPeriodRequest, long id);
    void deleteIrrigationPlanPeriodByID(long id);
}
