package com.springboot.backend.service.impl;

import com.springboot.backend.exception.ResourceNotFoundException;
import com.springboot.backend.model.*;
import com.springboot.backend.repository.*;
import com.springboot.backend.request.AddPlanPeriodRequest;
import com.springboot.backend.request.UpdateIrrigationPlanPeriodRequest;
import com.springboot.backend.service.IrrigationPlanPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IrrigationPlanPeriodServiceImpl implements IrrigationPlanPeriodService {

    @Autowired
    private IrrigationPlanPeriodRepository irrigationPlanPeriodRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IrrigationPlanRepository irrigationPlanRepository;

    public IrrigationPlanPeriodServiceImpl(IrrigationPlanPeriodRepository irrigationPlanPeriodRepository) {
        super();
        this.irrigationPlanPeriodRepository = irrigationPlanPeriodRepository;
    }

    @Override
    public IrrigationPlanPeriod addIrrigationPlanPeriod(AddPlanPeriodRequest addPlanPeriodRequest) {
        User user = userRepository.getUserById(addPlanPeriodRequest.getUser_id());
        IrrigationPlan irrigationPlan = irrigationPlanRepository.findIrrigationPlanById(addPlanPeriodRequest.getIrrigation_plan_id());
        IrrigationPlanPeriod addPlanPeriod = new IrrigationPlanPeriod();

        addPlanPeriod.setIrrigation_plan_period_name(addPlanPeriodRequest.getPeriod_name());
        addPlanPeriod.setStart_date(addPlanPeriodRequest.getPeriod_start_date());
        addPlanPeriod.setEnd_date(addPlanPeriodRequest.getPeriod_end_date());
        addPlanPeriod.setIrrigationPlan(irrigationPlan);
        addPlanPeriod.setUser(user);
        return irrigationPlanPeriodRepository.save(addPlanPeriod);
    }

    @Override
    public List<IrrigationPlanPeriod> getIrrigationPlanPeriods() {
        return irrigationPlanPeriodRepository.findAll();
    }

    @Override
    public IrrigationPlanPeriod getIrrigationPlanPeriodByID(long id) {
        return irrigationPlanPeriodRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("IrrigationPlanPeriod", "ID",id));
    }

    @Override
    public IrrigationPlanPeriod updateIrrigationPlanPeriod(UpdateIrrigationPlanPeriodRequest updateIrrigationPlanPeriodRequest, long id) {
        return null;
    }

    @Override
    public void deleteIrrigationPlanPeriodByID(long id) {

    }
}
