package com.springboot.backend.service.impl;

import com.springboot.backend.exception.ResourceNotFoundException;
import com.springboot.backend.model.Field;
import com.springboot.backend.model.User;
import com.springboot.backend.repository.FieldRepository;
import com.springboot.backend.repository.UserRepository;
import com.springboot.backend.request.AddFieldRequest;
import com.springboot.backend.request.UpdateFieldRequest;
import com.springboot.backend.service.FieldService;
import com.springboot.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class FieldServiceImpl implements FieldService {
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private UserRepository userRepository;

    public FieldServiceImpl(FieldRepository fieldRepository) {
        super();
        this.fieldRepository = fieldRepository;
    }
    @Override
    public Field addField(Field field) {

        Field addField = new Field();
        User user = new User();

        addField.setField_name(field.getField_name());
        addField.setField_location(field.getField_location());
        addField.setField_size(field.getField_size());
        addField.setField_size_unit(field.getField_size_unit());

        return fieldRepository.save(addField);
    }

    @Override
    public List<Field> getALlFields() {
        return fieldRepository.findAll();
    }
    @Override
    public Field getFieldByID(long id) {
        return fieldRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Field", "ID",id));
    }
    @Override
    public Field updateField(UpdateFieldRequest updateFieldRequest, long id) {

        Field fieldExists = fieldRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("field", "Id", id));

        fieldExists.setField_name(updateFieldRequest.getField_name());
        fieldExists.setField_location(updateFieldRequest.getField_location());
        fieldExists.setField_size(updateFieldRequest.getField_size());
        fieldExists.setField_size_unit(updateFieldRequest.getField_size_unit());
        fieldRepository.save(fieldExists);

        return fieldExists;
    }

    @Override
    public Field editField(Field field, long id) {

        Field fieldExists = fieldRepository.findFieldById(id);
        fieldExists.setField_name(field.getField_name());
        fieldExists.setField_location(field.getField_location());
        fieldExists.setField_size(field.getField_size());
        fieldExists.setField_size_unit(field.getField_size_unit());

        return fieldRepository.save(fieldExists);
    }
    @Override
    public void deleteFieldByID(long id) {
        //check if user exists with that ID
        fieldRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Field","Id",id));
        fieldRepository.deleteById(id);
    }

    @Override
    public Field saveField(AddFieldRequest addFieldRequest) {
        User user = userRepository.getUserById(addFieldRequest.getUser_id());
        Field addField = new Field();

        addField.setField_name(addFieldRequest.getField_name());
        addField.setField_location(addFieldRequest.getField_location());
        addField.setField_size(addFieldRequest.getField_size());
        addField.setField_size_unit(addFieldRequest.getField_size_unit());
        addField.setUser(user);

        return fieldRepository.save(addField);

    }


}
