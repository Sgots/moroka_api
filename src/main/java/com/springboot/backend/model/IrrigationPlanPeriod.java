package com.springboot.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.sql.Time;
import java.util.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_irrigation_plan_period")
public class IrrigationPlanPeriod{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "irrigation_period_id", nullable = false)
    private long id;

    String irrigation_plan_period_name;
    String start_date;
    String end_date;

    @JsonBackReference
    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "irrigation_plan_id")
    private IrrigationPlan irrigationPlan;


    @JsonIgnore
    @OneToMany(targetEntity = IrrigationPlanPeriodTimes.class, mappedBy = "irrigationPlanPeriod", fetch = FetchType.EAGER)
    private Set<IrrigationPlanPeriodTimes> irrigationPlanPeriodTimes = new HashSet<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    private int delete_status = 0;


    /*   public void addToPeriodTimes(IrrigationPlanPeriodTimes irrigationPlanPeriodTimes) {
        this.irrigationPlanPeriodTimes.add(irrigationPlanPeriodTimes);
        irrigationPlanPeriodTimes.setIrrigationPlanPeriod(this);
    }*/
}
