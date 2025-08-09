package com.springboot.backend.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class AddIrrigationPlanRequest implements Serializable {
    private long irrigation_plan_id;
    private String irrigation_plan_name;
    private String irrigation_plan_description;

    private String start_date;
    private String end_date;
    private long irrigation_period_id;
    private String period_name;
    private String period_start_date;
    private String period_end_date;

    private long irrigation_plan_period_time_id;
    private int moisture_target;
    private String period_start_time;
    private String period_end_time;

    private long user_id;
    private long field_id;
    private long area_id;
    private long patch_id;
    private long plan_period_id;
}
