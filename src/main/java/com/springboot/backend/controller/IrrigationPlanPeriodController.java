package com.springboot.backend.controller;

import com.springboot.backend.exception.MessageResponse;
import com.springboot.backend.model.IrrigationPlan;
import com.springboot.backend.model.IrrigationPlanPeriod;
import com.springboot.backend.repository.IrrigationPlanPeriodRepository;
import com.springboot.backend.repository.IrrigationPlanRepository;
import com.springboot.backend.request.AddIrrigationPlanRequest;
import com.springboot.backend.request.AddPlanPeriodRequest;
import com.springboot.backend.request.EditIrrigationPlanRequest;
import com.springboot.backend.request.UpdateIrrigationPlanPeriodRequest;
import com.springboot.backend.service.IrrigationPlanPeriodService;
import com.springboot.backend.service.IrrigationPlanService;
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
public class IrrigationPlanPeriodController {
    @Autowired
    private IrrigationPlanPeriodService irrigationPlanPeriodService;
    @Autowired
    private IrrigationPlanPeriodRepository irrigationPlanPeriodRepository;

    public IrrigationPlanPeriodController(IrrigationPlanPeriodService irrigationPlanPeriodService) {
        super();
        this.irrigationPlanPeriodService = irrigationPlanPeriodService;
    }

    @PostMapping("/add-period")
    public ResponseEntity <?> addIrrigationPlan(@Valid @RequestBody AddPlanPeriodRequest addPlanPeriodRequest) throws SQLIntegrityConstraintViolationException {
        if (irrigationPlanPeriodRepository.existsById(addPlanPeriodRequest.getIrrigation_plan_period_id())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Period exists!"));
        }
        return new ResponseEntity<IrrigationPlanPeriod>(irrigationPlanPeriodService.addIrrigationPlanPeriod(addPlanPeriodRequest), HttpStatus.OK);
    }

    @RequestMapping(value = "/get-plan-period/{id}", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<IrrigationPlanPeriod> getIrrigationPlanPeriodByID(@PathVariable("id") long periodID){
        return new ResponseEntity<IrrigationPlanPeriod>(irrigationPlanPeriodService.getIrrigationPlanPeriodByID(periodID), HttpStatus.OK);
    }
    @GetMapping("/get-plan-periods")
    public List<IrrigationPlanPeriod> getAllIrrigationPlanPeriod(){
        return irrigationPlanPeriodService.getIrrigationPlanPeriods();
    }

    @PostMapping("/update-plan-period/{id}")
    public ResponseEntity<IrrigationPlanPeriod> updatePlan(@PathVariable ("id") long periodID,
                                                     @RequestBody UpdateIrrigationPlanPeriodRequest updateIrrigationPlanPeriodRequest){
        return new ResponseEntity<IrrigationPlanPeriod>(irrigationPlanPeriodService.updateIrrigationPlanPeriod(updateIrrigationPlanPeriodRequest, periodID), HttpStatus.OK);
    }

    @PostMapping("/delete-plan-period/{id}")
    public ResponseEntity<String> deleteIrrigationPlanPeriod(@PathVariable("id") long periodID){
        irrigationPlanPeriodRepository.markIrrigationPlanPeriodAsDeleted(periodID);
        return new ResponseEntity<String>("Irrigation Plan Period Deleted",HttpStatus.OK);
    }

}
