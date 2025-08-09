package com.springboot.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_irrigation_plan_period_times")
public class IrrigationPlanPeriodTimes{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "irrigation_plan_period_time_id", nullable = false)
    long id;

    @JsonBackReference
    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name="irrigation_period_id", referencedColumnName = "irrigation_period_id")
    private IrrigationPlanPeriod irrigationPlanPeriod;

    String start_time = "N/A";
    String end_time = "N/A";
    double moisture_target = 0;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int delete_status = 0;

}
