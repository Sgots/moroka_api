package com.springboot.backend.controller;

import com.springboot.backend.exception.MessageResponse;
import com.springboot.backend.exception.NotFoundException;
import com.springboot.backend.model.*;
import com.springboot.backend.repository.FieldRepository;
import com.springboot.backend.repository.IrrigationPlanRepository;
import com.springboot.backend.repository.PatchRepository;
import com.springboot.backend.request.AddIrrigationPlanRequest;
import com.springboot.backend.request.AddPatchRequest;
import com.springboot.backend.request.EditIrrigationPlanRequest;
import com.springboot.backend.service.IrrigationPlanService;
import com.springboot.backend.service.PatchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class IrrigationPlanController {

    @Autowired
    private IrrigationPlanService irrigationPlanService;
    @Autowired
    private IrrigationPlanRepository irrigationPlanRepository;

    public IrrigationPlanController(IrrigationPlanRepository irrigationPlanRepository, IrrigationPlanService irrigationPlanService) {
        super();
        this.irrigationPlanService = irrigationPlanService;
        this.irrigationPlanRepository = irrigationPlanRepository;

    }

    @PutMapping("attach-plan/{fieldId}/{planId}")
    public ResponseEntity<Field> attachIrrigationPlanToField(
            @PathVariable("fieldId") long fieldId,
            @PathVariable("planId") long planId) {

        Field field = irrigationPlanService.attachPlan(fieldId, planId);
        return ResponseEntity.ok(field);
    }

    //add patch REST API
    //http://localhost:8080/api/add-irrigation-plan
    @PostMapping("/add-irrigation-plan")
    public ResponseEntity <?> addIrrigationPlan(@Valid @RequestBody AddIrrigationPlanRequest addIrrigationPlanRequest) throws SQLIntegrityConstraintViolationException {
        if (irrigationPlanRepository.existsById(addIrrigationPlanRequest.getIrrigation_plan_id())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Irrigation Plan exists!"));
        }
        return new ResponseEntity<IrrigationPlan>(irrigationPlanService.addIrrigationPlan(addIrrigationPlanRequest), HttpStatus.OK);
    }

    @RequestMapping(value = "/get-irrigation-plan/{id}", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<IrrigationPlan> getIrrigationPlanByID(@PathVariable("id") long planID){
        return new ResponseEntity<IrrigationPlan>(irrigationPlanRepository.findIrrigationPlanById(planID),HttpStatus.OK);
    }


    @PostMapping("/update-irrigation-plan/{id}")
    public ResponseEntity<IrrigationPlan> updatePlan(@PathVariable ("id") long planID,
                                             @RequestBody EditIrrigationPlanRequest irrigationPlan){
        return new ResponseEntity<IrrigationPlan>(irrigationPlanService.updateIrrigationPlan(irrigationPlan, planID), HttpStatus.OK);

    }

    //create get field by ID REST API
    //http://localhost:8080/api/field/1
    @GetMapping("/get-irrigation-plans")
    public List <IrrigationPlan> getAllIrrigationPlans(){
        return irrigationPlanService.getIrrigationPlans();
    }

    @GetMapping("/get-user-plans/{id}")
    public List <IrrigationPlan> getUserPlans(@PathVariable ("id") long userID){
        return irrigationPlanRepository.getUserPlans(userID);
    }

    @GetMapping("/viewFieldPlan/{id}")
    public List <IrrigationPlan> viewFieldPlan(@PathVariable ("id") long planID){
        return irrigationPlanService.getFieldPlan(planID);
    }

    @PostMapping("/delete-irrigation-plan/{id}")
    public ResponseEntity<String> deleteIrrigationPlan(@PathVariable("id") long planID){
        irrigationPlanRepository.markIrrigationPlanAsDeleted(planID);
        return new ResponseEntity<String>("Irrigation Plan Deleted",HttpStatus.OK);
    }
}
