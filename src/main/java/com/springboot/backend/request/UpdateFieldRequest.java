package com.springboot.backend.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateFieldRequest implements Serializable {
    private String field_name;
    private String field_location;
    private double field_size;
    private String field_size_unit;
}
