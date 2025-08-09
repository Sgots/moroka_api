package com.springboot.backend.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class AddAreaRequest implements Serializable {
    private int area_id;
    private String area_name;
    private double area_size;
    private long field_id;
    private long user_id;
}
