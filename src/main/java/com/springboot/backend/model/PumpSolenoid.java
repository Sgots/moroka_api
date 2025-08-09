package com.springboot.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "tbl_pump_solenoid")
public class PumpSolenoid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flow_start")
    private String flowStart;

    @Column(name = "flow_end")
    private String flowEnd;

    @JsonBackReference
    //@JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "device_id", referencedColumnName = "device_id")
    private Set<DataResponse> dataResponse = new HashSet<>();
    @Column(name = "device_id")
    private String device_id;

    @Column(name = "timestamp")
    private Timestamp timestamp;
}
