package com.springboot.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Table(name = "tbl_flow_meter")
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlowMeter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flow_rate")
    private String flowRate;

    @Column(name = "dispensed_water")
    private String dispensedWater;

    @JsonBackReference
    //@JsonIgnore
    @ManyToMany()
    @JoinColumn(name = "device_id", referencedColumnName = "deviceID")
    private Set<DataResponse> dataResponse = new HashSet<>();
    @Column(name = "device_id")
    private String device_id;
    @Column(name = "timestamp")
    private Timestamp timestamp;
}
