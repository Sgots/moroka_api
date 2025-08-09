package com.springboot.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.util.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_irrigation_plans")
public class IrrigationPlan implements Serializable {
    //private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "irrigation_plan_id", nullable = false)
    private long id;
    private String plan_name;
    private String plan_description;

    //TODO  child tables - mapping!
    //mapping
    @JsonIgnore
    @JsonManagedReference
    @OneToMany(targetEntity=IrrigationPlanPeriod.class, mappedBy="irrigationPlan")
    private Set<IrrigationPlanPeriod> irrigationPlanPeriods = new HashSet<>();

    //mapping
    @JsonIgnore
    @ManyToOne( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private Field fields;
    //mapping
    @JsonIgnore
    @OneToMany(mappedBy = "irrigationPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Area> areas = new ArrayList<>();

    //mapping
    @JsonIgnore
    @OneToMany(mappedBy = "irrigationPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Patch> patches = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    private int delete_status = 0;

 /*   public void addToPeriodPlan(IrrigationPlanPeriod irrigationPlanPeriod) {
        this.irrigationPlanPeriods.add(irrigationPlanPeriod);
        irrigationPlanPeriod.setIrrigationPlan(this);
    }*/
}
