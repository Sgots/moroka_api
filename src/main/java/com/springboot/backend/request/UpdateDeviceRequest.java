package com.springboot.backend.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateDeviceRequest implements Serializable {

    private long device_id;
    private String device_name;
}
