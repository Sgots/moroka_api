package com.springboot.backend.service;

import com.springboot.backend.model.Field;
import com.springboot.backend.model.User;
import com.springboot.backend.request.AddFieldRequest;
import com.springboot.backend.request.UpdateFieldRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FieldService {

    Field addField(Field field);
    List<Field> getALlFields();
    Field getFieldByID(long id);
    Field updateField(UpdateFieldRequest updateFieldRequest, long id);

    Field editField(Field field, long id);

    //Field deleteField(long id);
    void deleteFieldByID(long id);

    Field saveField(AddFieldRequest addFieldRequest);
}
