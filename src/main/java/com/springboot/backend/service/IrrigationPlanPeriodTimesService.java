package com.springboot.backend.service;

import com.springboot.backend.model.IrrigationPlanPeriodTimes;
import com.springboot.backend.request.AddPlanPeriodTimesRequest;
import com.springboot.backend.request.UpdateIrrigationPlanPeriodRequest;
import com.springboot.backend.request.UpdateIrrigationPlanPeriodTimesRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IrrigationPlanPeriodTimesService {

    IrrigationPlanPeriodTimes addIrrigationPlanPeriodTime(AddPlanPeriodTimesRequest addPlanPeriodTimesRequest);
    List<IrrigationPlanPeriodTimes> getIrrigationPlanPeriodTimes();
    IrrigationPlanPeriodTimes getIrrigationPlanPeriodTimesByID(long id);
    IrrigationPlanPeriodTimes updateIrrigationPlanPeriodTimes(UpdateIrrigationPlanPeriodTimesRequest updateIrrigationPlanPeriodTimesRequest, long id);
    void deleteIrrigationPlanPeriodTimeByID(long id);
}
