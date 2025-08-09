package com.springboot.backend.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
@Data
public class AddPatchRequest implements Serializable {

    private long patch_id;
    private String patch_name;
    private double patch_size;
    private long area_id;
    private long field_id;
    private long user_id;
}
