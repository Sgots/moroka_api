package com.springboot.backend.service.impl;

import com.springboot.backend.exception.ResourceNotFoundException;
import com.springboot.backend.model.*;
import com.springboot.backend.repository.*;
import com.springboot.backend.request.AddIrrigationPlanRequest;
import com.springboot.backend.request.EditIrrigationPlanRequest;
import com.springboot.backend.service.IrrigationPlanService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.springboot.backend.MQTT.MqttSubscriber.subscribeToTopic;

@Service
public class IrrigationPlanServiceImpl implements IrrigationPlanService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IrrigationPlanService irrigationPlanService;
    @Autowired
    private IrrigationPlanRepository irrigationPlanRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private IrrigationPlanPeriodRepository irrigationPlanPeriodRepository;

    @Autowired
    private IrrigationPlanPeriodTimesRepository irrigationPlanPeriodTimesRepository;

    public IrrigationPlanServiceImpl(IrrigationPlanRepository irrigationPlanRepository) {
        super();
        this.irrigationPlanRepository = irrigationPlanRepository;
    }

    @Override
    public IrrigationPlan addIrrigationPlan(AddIrrigationPlanRequest addIrrigationPlanRequest) {

        //capture ID's
        User user = userRepository.getUserById(addIrrigationPlanRequest.getUser_id());
        IrrigationPlanPeriod irrigationPlanPeriod = irrigationPlanPeriodRepository.findIrrigationPlanPeriodById((addIrrigationPlanRequest.getPlan_period_id()));
        IrrigationPlan irrigationPlan = irrigationPlanRepository.findIrrigationPlanById(addIrrigationPlanRequest.getIrrigation_plan_id());
        IrrigationPlanPeriodTimes irrigationPlanPeriodTimes = irrigationPlanPeriodTimesRepository.findIrrigationPlanPeriodTimesById(addIrrigationPlanRequest.getIrrigation_plan_period_time_id());

        //Create Instances
        IrrigationPlan addIrrigationPlan = new IrrigationPlan();
        IrrigationPlanPeriod addIrrigationPlanPeriod = new IrrigationPlanPeriod();
        IrrigationPlanPeriodTimes addIrrigationPlanPeriodTimes = new IrrigationPlanPeriodTimes();

        //set values
        addIrrigationPlan.setPlan_name(addIrrigationPlanRequest.getIrrigation_plan_name());
        addIrrigationPlan.setPlan_description(addIrrigationPlanRequest.getIrrigation_plan_description());
        addIrrigationPlan.setUser(user);
        irrigationPlanRepository.save(addIrrigationPlan);

        addIrrigationPlanPeriod.setIrrigation_plan_period_name(addIrrigationPlanRequest.getPeriod_name());
        addIrrigationPlanPeriod.setStart_date(addIrrigationPlanRequest.getPeriod_start_date());
        addIrrigationPlanPeriod.setEnd_date(addIrrigationPlanRequest.getPeriod_end_date());
        //addIrrigationPlan.addToPeriodPlan(addIrrigationPlanPeriod);
        addIrrigationPlanPeriod.setIrrigationPlan(addIrrigationPlan);
        addIrrigationPlanPeriod.setUser(user);
        irrigationPlanPeriodRepository.save(addIrrigationPlanPeriod);

        addIrrigationPlanPeriodTimes.setStart_time(addIrrigationPlanRequest.getPeriod_start_time());
        addIrrigationPlanPeriodTimes.setEnd_time(addIrrigationPlanRequest.getPeriod_end_time());
        addIrrigationPlanPeriodTimes.setMoisture_target(addIrrigationPlanRequest.getMoisture_target());
        //addIrrigationPlanPeriod.addToPeriodTimes(addIrrigationPlanPeriodTimes);
        addIrrigationPlanPeriodTimes.setIrrigationPlanPeriod(addIrrigationPlanPeriod);
        addIrrigationPlanPeriodTimes.setUser(user);
        irrigationPlanPeriodTimesRepository.save(addIrrigationPlanPeriodTimes);

        return irrigationPlanRepository.save(irrigationPlan);
    }



    @Override
    public List<IrrigationPlan> getIrrigationPlans() {
        return irrigationPlanRepository.findAll();
    }

    @Override
    public List<IrrigationPlan> getFieldPlan(long id) {
        return irrigationPlanService.getFieldPlan( id);
    }

    @Override
    public List<IrrigationPlan> getIrrigationPlansByUserID(long id) {
        return null;
    }

    @Override
    public IrrigationPlan getIrrigationPlanByID(long id) {
        return null;
    }

    @Override
    public IrrigationPlan updateIrrigationPlan(EditIrrigationPlanRequest editIrrigationPlanRequest, long id) {
        IrrigationPlan planExists = irrigationPlanRepository.findIrrigationPlanById(id);
        planExists.setPlan_name(editIrrigationPlanRequest.getIrrigation_plan_name());
        planExists.setPlan_description(editIrrigationPlanRequest.getIrrigation_plan_description());

        return irrigationPlanRepository.save(planExists);
    }

    @Override
    public void deleteIrrigationPlanByID(long id) {
    }
    @Override
    public Field attachPlan(long fieldId, long planId) {
        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new EntityNotFoundException("Field not found with ID: " + fieldId));

        IrrigationPlan irrigationPlan = irrigationPlanRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("Irrigation Plan not found with ID: " + planId));

        field.setIrrigationPlan(irrigationPlan);

        return fieldRepository.save(field);
    }
    // src/main/java/.../service/IrrigationPlanService.java

        public IrrigationPlanDTO getPlanWithDates(long id) {
            Map<String,Object> row = irrigationPlanRepository.findPlanWithDatesRaw(id);
            if (row == null) return null;
            return new IrrigationPlanDTO(
                    ((Number)row.get("id")).longValue(),
                    (String) row.get("plan_name"),
                    (String) row.get("plan_description"),
                    row.get("delete_status") == null ? 0 : ((Number)row.get("delete_status")).intValue(),
                    (String) row.get("start_date"),  // can be null
                    (String) row.get("end_date")     // can be null
            );
        }
    }

