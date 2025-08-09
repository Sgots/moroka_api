package com.springboot.backend.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class EditIrrigationPlanRequest  implements Serializable {
    private String irrigation_plan_name;
    private String irrigation_plan_description;

}
