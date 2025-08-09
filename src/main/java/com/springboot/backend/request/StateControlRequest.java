package com.springboot.backend.request;

import lombok.Data;

@Data
public class StateControlRequest {
    private String deviceID;
    private int fieldID;
}
