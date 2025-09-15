package com.springboot.backend.model;

// src/main/java/.../dto/IrrigationPlanDTO.java

public class IrrigationPlanDTO {
    public Long   id;
    public String plan_name;
    public String plan_description;
    public Integer delete_status;
    public String start_date; // keep as String because your schema stores varchar(255)
    public String end_date;

    public IrrigationPlanDTO(Long id, String planName, String planDesc, Integer deleteStatus,
                             String startDate, String endDate) {
        this.id = id;
        this.plan_name = planName;
        this.plan_description = planDesc;
        this.delete_status = deleteStatus;
        this.start_date = startDate;
        this.end_date = endDate;
    }
}
