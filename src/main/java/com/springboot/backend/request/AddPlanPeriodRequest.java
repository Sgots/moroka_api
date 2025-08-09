package com.springboot.backend.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddPlanPeriodRequest implements Serializable {
    private long irrigation_plan_period_id;
    private String period_name;
    private String period_start_date;
    private String period_end_date;
    private long user_id;
    private long irrigation_plan_id;
}
