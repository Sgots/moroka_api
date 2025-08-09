package com.springboot.backend.controller;

import com.springboot.backend.exception.MessageResponse;
import com.springboot.backend.model.Field;
import com.springboot.backend.repository.FieldRepository;
import com.springboot.backend.request.AddFieldRequest;
import com.springboot.backend.request.UpdateFieldRequest;
import com.springboot.backend.service.FieldService;
import com.springboot.backend.service.impl.FieldServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class FieldController {
    @Autowired
    private FieldService fieldService;
    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private FieldServiceImpl fieldServiceimpl;

    public FieldController(FieldService fieldService) {
        super();
        this.fieldService = fieldService;
    }
    //add field REST API
    //http://localhost:8080//api/add-field
    @PostMapping("/add-field")
    public Field addField(@Valid @RequestBody AddFieldRequest addFieldRequest) throws SQLIntegrityConstraintViolationException {
        return fieldService.saveField(addFieldRequest);
    }

    //create get fields REST API
    //http://localhost:8080//api/get-fields
    @GetMapping("/get-fields")
    public List<?> getFields(){
        return fieldService.getALlFields();
    }

    //create get field by ID REST API
    //http://localhost:8080/api/field/1
    @RequestMapping(value = "/get-field/{id}", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<Field> getFieldByID(@PathVariable("id") int fieldId){
        return new ResponseEntity<Field>(fieldService.getFieldByID(fieldId),HttpStatus.OK);
    }
    //update field by ID REST API
    //http://localhost:8080/api/field/1
    @PostMapping("/update-field/{id}")
    public ResponseEntity<Field> updateField(@PathVariable ("id") long fieldID,
                                            @RequestBody UpdateFieldRequest field){
        return new ResponseEntity<Field>(fieldService.updateField(field, fieldID), HttpStatus.OK);

    }


    @RequestMapping(value = "/get-field-data/{fid}/{did}/{lid}", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<Field> getFieldByData(@PathVariable("id") int fieldId){
        return new ResponseEntity<Field>(fieldService.getFieldByID(fieldId),HttpStatus.OK);
    }

    @RequestMapping(value = "/edit-field/{id}", method = RequestMethod.POST)
    public ResponseEntity<Field> editField(@PathVariable("id") long fieldId,
                                           @RequestBody Field field){
        return new ResponseEntity<Field>(fieldService.editField(field, fieldId),HttpStatus.OK);

    }
    //delete field by ID REST API
    //http://localhost:8080/api/field/1
    @PostMapping("/delete-field/{id}")
    public ResponseEntity<String> deleteField(@PathVariable("id") long fieldID){
        //delete field from DB
        fieldRepository.markFieldAsDeleted(fieldID);
        return new ResponseEntity<String>("Field Deleted Successfully",HttpStatus.OK);
    }
}
