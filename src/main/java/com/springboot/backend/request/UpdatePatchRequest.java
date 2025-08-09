package com.springboot.backend.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdatePatchRequest implements Serializable {
    private String patch_name;
    private double patch_size;

}
