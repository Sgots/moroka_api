package com.springboot.backend.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddPlanPeriodTimesRequest implements Serializable {

    private long time_id;
    private long irrigation_plan_period_id;
    private int moisture_target = 0;
    private String period_start_time = "N/A";
    private String period_end_time = "N/A";
}
