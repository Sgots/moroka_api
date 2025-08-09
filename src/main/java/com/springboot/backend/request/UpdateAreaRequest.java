package com.springboot.backend.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateAreaRequest implements Serializable {

    private String area_name;
    private double area_size;

}
