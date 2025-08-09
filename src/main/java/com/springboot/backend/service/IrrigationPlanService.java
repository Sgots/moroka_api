package com.springboot.backend.service;

import com.springboot.backend.model.Device;
import com.springboot.backend.model.Field;
import com.springboot.backend.model.IrrigationPlan;
import com.springboot.backend.request.AddIrrigationPlanRequest;
import com.springboot.backend.request.EditIrrigationPlanRequest;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface IrrigationPlanService {

    IrrigationPlan addIrrigationPlan(AddIrrigationPlanRequest addIrrigationPlanRequest);

    Field attachPlan(long fieldID, long planID);

    List<IrrigationPlan> getIrrigationPlans();
    List<IrrigationPlan> getFieldPlan(long ig);

    List <IrrigationPlan> getIrrigationPlansByUserID(long id);
    IrrigationPlan getIrrigationPlanByID(long id);
    IrrigationPlan updateIrrigationPlan(EditIrrigationPlanRequest editIrrigationPlanRequest, long id);
    void deleteIrrigationPlanByID(long id);

}
