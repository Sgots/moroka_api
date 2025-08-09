package com.springboot.backend.controller;

import com.springboot.backend.exception.MessageResponse;
import com.springboot.backend.model.IrrigationPlanPeriod;
import com.springboot.backend.model.IrrigationPlanPeriodTimes;
import com.springboot.backend.repository.IrrigationPlanPeriodRepository;
import com.springboot.backend.repository.IrrigationPlanPeriodTimesRepository;
import com.springboot.backend.request.AddPlanPeriodRequest;
import com.springboot.backend.request.AddPlanPeriodTimesRequest;
import com.springboot.backend.service.IrrigationPlanPeriodService;
import com.springboot.backend.service.IrrigationPlanPeriodTimesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class IrrigationPlanPeriodTimesController {
    @Autowired
    private IrrigationPlanPeriodTimesService irrigationPlanPeriodTimesService;
    @Autowired
    private IrrigationPlanPeriodTimesRepository irrigationPlanPeriodTimesRepository;

    public IrrigationPlanPeriodTimesController(IrrigationPlanPeriodTimesService irrigationPlanPeriodTimesService) {
        super();
        this.irrigationPlanPeriodTimesService = irrigationPlanPeriodTimesService;
    }

    @PostMapping("/add-times")
    public ResponseEntity<?> addIrrigationPlan(@Valid @RequestBody AddPlanPeriodTimesRequest addPlanPeriodTimesRequest) throws SQLIntegrityConstraintViolationException {
        if (irrigationPlanPeriodTimesRepository.existsById(addPlanPeriodTimesRequest.getTime_id())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Period Time exists!"));
        }
        return new ResponseEntity<IrrigationPlanPeriodTimes>(irrigationPlanPeriodTimesService.addIrrigationPlanPeriodTime(addPlanPeriodTimesRequest), HttpStatus.OK);
    }

}
