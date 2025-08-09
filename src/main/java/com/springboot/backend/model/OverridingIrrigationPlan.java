package com.springboot.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;

@Entity
@Data
@Table(name = ("tbl_overriding_irrigation_plan"))
public class OverridingIrrigationPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "overriding_irrigation_plan_id")
    private long id;

    private Time plan_start_time;
    private Time plan_end_time;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
