package com.springboot.backend.controller;

import com.springboot.backend.exception.MessageResponse;
import com.springboot.backend.model.Area;
import com.springboot.backend.model.Device;
import com.springboot.backend.model.Field;
import com.springboot.backend.repository.AreaRepository;
import com.springboot.backend.request.AddAreaRequest;
import com.springboot.backend.request.UpdateAreaRequest;
import com.springboot.backend.request.UpdateDeviceRequest;
import com.springboot.backend.service.AreaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class AreaController {

    @Autowired
    private AreaService areaService;
    @Autowired
    private AreaRepository areaRepository;

    public AreaController(AreaService areaService) {
        super();
        this.areaService = areaService;
    }
    //add area REST API
    //http://localhost:8080//api/add-area
    @PostMapping("/add-area")
    public Area addArea(@Valid @RequestBody AddAreaRequest addAreaRequest) throws SQLIntegrityConstraintViolationException {
        return areaService.saveArea(addAreaRequest);
    }
    //create get area REST API
    //http://localhost:8080//api/get-area
    @GetMapping("/get-areas")
    public List<?> getAreas(){
        return areaService.getALlAreas();
    }


    //create get area by ID REST API
    //http://localhost:8080/api/area/1
    @GetMapping("/get-area/{area_id}")
    public ResponseEntity<Area> getAreaByID(@PathVariable("area_id") int areaId){
        return new ResponseEntity<Area>(areaService.getAreaByID(areaId),HttpStatus.OK);
    }

    //create get area by field ID REST API
    //http://localhost:8080/api/v1/area/1/1
    @GetMapping("/get-area-field/{field_id}")
    public List<?> findAreaByFiledID(@Valid @RequestBody @PathVariable("field_id") long fieldId){
        return areaService.findAreabyFiedID(fieldId);
    }

    @GetMapping("/find-field-area/{area_id}")
    public Field findFieldAreaByAreaID(@PathVariable("area_id") int areaID){
        return areaRepository.findFieldAreaByAreaId(areaID);
    }

    @GetMapping("/find-area/{area_id}")
    public Area findArea(@PathVariable("area_id") int areaID){
        return areaRepository.findArea(areaID);
    }

    @GetMapping("/get-field-area/{area_id}")
    public Field findFieldArea(@PathVariable("area_id") int areaID){
        return areaRepository.findFieldAreaByAreaId(areaID);
    }

    //update area by ID REST API
    //http://localhost:8080/api/area/1
    @PostMapping("/update-area/{area_id}")
    public ResponseEntity<Area> updateArea(@PathVariable ("area_id") int areaID,
                                            @RequestBody UpdateAreaRequest updateAreaRequest){
        return new ResponseEntity<Area>(areaService.updateArea(updateAreaRequest, areaID), HttpStatus.OK);

    }


    //delete area by ID REST API
    //http://localhost:8080/api/area/1
    @PostMapping("/delete-area/{id}")
    public ResponseEntity<String> deleteAreaByID(@PathVariable("id") long areaID){
        //delete area from DB
        areaRepository.markAreaAsDeleted(areaID);
        //areaService.deleteAreaByID(areaID);
        return new ResponseEntity<String>("Area Deleted Successfully",HttpStatus.OK);
    }

}
