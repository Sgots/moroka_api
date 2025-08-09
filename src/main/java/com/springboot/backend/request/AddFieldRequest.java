package com.springboot.backend.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class AddFieldRequest implements Serializable {
    private long id;
    private String field_name;
    private String field_location;
    private double field_size;
    private String field_size_unit;
    private long user_id;
}
