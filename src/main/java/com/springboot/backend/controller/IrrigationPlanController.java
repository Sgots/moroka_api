package com.springboot.backend.controller;

import com.springboot.backend.exception.MessageResponse;
import com.springboot.backend.exception.NotFoundException;
import com.springboot.backend.model.*;
import com.springboot.backend.repository.AreaRepository;
import com.springboot.backend.repository.FieldRepository;
import com.springboot.backend.repository.IrrigationPlanRepository;
import com.springboot.backend.repository.PatchRepository;
import com.springboot.backend.request.AddIrrigationPlanRequest;
import com.springboot.backend.request.AddPatchRequest;
import com.springboot.backend.request.EditIrrigationPlanRequest;
import com.springboot.backend.service.IrrigationPlanService;
import com.springboot.backend.service.PatchService;
import com.springboot.backend.service.impl.IrrigationPlanServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class IrrigationPlanController {
    @Autowired
    private  IrrigationPlanRepository planRepo;
    @Autowired
    private  FieldRepository fieldRepo;
    @Autowired
    private AreaRepository areaRepo;
    @Autowired
    private  PatchRepository patchRepo;

    @Autowired
    private IrrigationPlanService irrigationPlanService;
    @Autowired
    private IrrigationPlanRepository irrigationPlanRepository;
@Autowired
    private IrrigationPlanServiceImpl irrigationPlanServiceImp;
    public IrrigationPlanController(IrrigationPlanRepository irrigationPlanRepository, IrrigationPlanService irrigationPlanService) {
        super();
        this.irrigationPlanService = irrigationPlanService;
        this.irrigationPlanRepository = irrigationPlanRepository;

    }



    @PutMapping(
            value = "/attach-plan",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<AttachPlanResponse> attachPlan(@RequestBody AttachPlanRequest req) {

        if (req.planId() == null) {
            throw new ResponseStatusException(BAD_REQUEST, "planId is required");
        }

        IrrigationPlan plan = planRepo.findById(req.planId())
                .orElseThrow(() -> new EntityNotFoundException("Plan not found: " + req.planId()));

        Field field = null;
        Area  area  = null;
        Patch patch = null;

        // FIELD (optional)
        if (req.fieldId() != null) {
            field = fieldRepo.findById(req.fieldId())
                    .orElseThrow(() -> new EntityNotFoundException("Field not found: " + req.fieldId()));
        }

        // AREA (optional)
        if (req.areaId() != null) {
            area = areaRepo.findById(req.areaId().intValue())
                    .orElseThrow(() -> new EntityNotFoundException("Area not found: " + req.areaId()));

            // If field also provided, validate area.belongsTo(field)
            if (field != null) {
                Long areaFieldId = (area.getField() != null ? area.getField().getId() : null);
                if (!Objects.equals(areaFieldId, field.getId())) {
                    throw new ResponseStatusException(
                            BAD_REQUEST, "Area " + req.areaId() + " does not belong to Field " + req.fieldId());
                }
            } else if (area.getField() != null) {
                // Derive field from area for later patch validation
                field = area.getField();
            }
        }

        // PATCH (optional)
        if (req.patchId() != null) {
            patch = patchRepo.findById(req.patchId())
                    .orElseThrow(() -> new EntityNotFoundException("Patch not found: " + req.patchId()));

            // Validate patch -> area (if area provided or derived)
            if (area != null) {
                Integer patchAreaId = (patch.getArea() != null ? patch.getArea().getId() : null);
                if (!Objects.equals(patchAreaId, area.getId())) {
                    throw new ResponseStatusException(
                            BAD_REQUEST, "Patch " + req.patchId() + " does not belong to Area " + req.areaId());
                }
            } else if (patch.getArea() != null) {
                // Derive area (and maybe field) from patch for consistency
                area = patch.getArea();
                if (field == null && area.getField() != null) {
                    field = area.getField();
                }
            }

            // Validate patch -> field (if field provided or derived)
            if (field != null) {
                Long patchFieldId = (patch.getField() != null ? patch.getField().getId() : null);
                if (!Objects.equals(patchFieldId, field.getId())) {
                    throw new ResponseStatusException(
                            BAD_REQUEST, "Patch " + req.patchId() + " does not belong to Field " + req.fieldId());
                }
            }
        }

        // Persist attachments ONLY to targets the client specified
        if (req.fieldId() != null) {
            field.setIrrigationPlan(plan);
            fieldRepo.save(field);
        }
        if (req.areaId() != null) {
            area.setIrrigationPlan(plan);
            areaRepo.save(area);
        }
        if (req.patchId() != null) {
            patch.setIrrigationPlan(plan);
            patchRepo.save(patch);
        }

        AttachPlanResponse resp = new AttachPlanResponse(
                req.planId(),
                field != null ? field.getId() : null,
                area  != null ? area.getId()  : null,
                patch != null ? patch.getId() : null,
                "Plan attached successfully");

        return ResponseEntity.ok(resp);
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

    @GetMapping(value = "/get-irrigation-plan/{id}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getIrrigationPlanByID(@PathVariable("id") long planID){
        IrrigationPlanDTO dto = irrigationPlanServiceImp.getPlanWithDates(planID);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
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
