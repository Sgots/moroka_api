package com.springboot.backend.service.impl;

import com.springboot.backend.model.IrrigationPlan;
import com.springboot.backend.model.IrrigationPlanPeriod;
import com.springboot.backend.model.IrrigationPlanPeriodTimes;
import com.springboot.backend.model.User;
import com.springboot.backend.repository.IrrigationPlanPeriodRepository;
import com.springboot.backend.repository.IrrigationPlanPeriodTimesRepository;
import com.springboot.backend.repository.IrrigationPlanRepository;
import com.springboot.backend.repository.UserRepository;
import com.springboot.backend.request.AddPlanPeriodRequest;
import com.springboot.backend.request.AddPlanPeriodTimesRequest;
import com.springboot.backend.request.UpdateIrrigationPlanPeriodTimesRequest;
import com.springboot.backend.service.IrrigationPlanPeriodTimesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IrrigationPlanPeriodTimesImpl implements IrrigationPlanPeriodTimesService {
    @Autowired
    private IrrigationPlanPeriodTimesRepository irrigationPlanPeriodTimesRepository;

    @Autowired
    private IrrigationPlanRepository irrigationPlanRepository;
    @Autowired
    private IrrigationPlanPeriodRepository irrigationPlanPeriodRepository;

    public IrrigationPlanPeriodTimesImpl(IrrigationPlanPeriodTimesRepository irrigationPlanPeriodTimesRepository, IrrigationPlanPeriodRepository irrigationPlanPeriodRepository) {
        super();
        this.irrigationPlanPeriodTimesRepository = irrigationPlanPeriodTimesRepository;
        this.irrigationPlanPeriodRepository = irrigationPlanPeriodRepository;
    }

    @Override
    public IrrigationPlanPeriodTimes addIrrigationPlanPeriodTime(AddPlanPeriodTimesRequest addPlanPeriodTimesRequest) {
        IrrigationPlanPeriod periodID = irrigationPlanPeriodRepository.findIrrigationPlanPeriodById(addPlanPeriodTimesRequest.getIrrigation_plan_period_id());
        IrrigationPlanPeriodTimes addTimes = new IrrigationPlanPeriodTimes();

        addTimes.setIrrigationPlanPeriod(periodID);
        addTimes.setStart_time(addPlanPeriodTimesRequest.getPeriod_start_time());
        addTimes.setEnd_time(addPlanPeriodTimesRequest.getPeriod_end_time());
        addTimes.setMoisture_target(addPlanPeriodTimesRequest.getMoisture_target());
        return irrigationPlanPeriodTimesRepository.save(addTimes);
    }

    @Override
    public List<IrrigationPlanPeriodTimes> getIrrigationPlanPeriodTimes() {
        return null;
    }

    @Override
    public IrrigationPlanPeriodTimes getIrrigationPlanPeriodTimesByID(long id) {
        return null;
    }

    @Override
    public IrrigationPlanPeriodTimes updateIrrigationPlanPeriodTimes(UpdateIrrigationPlanPeriodTimesRequest updateIrrigationPlanPeriodTimesRequest, long id) {
        return null;
    }

    @Override
    public void deleteIrrigationPlanPeriodTimeByID(long id) {

    }

}
