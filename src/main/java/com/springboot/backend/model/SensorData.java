package com.springboot.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Table(name = "tbl_sensor_data")
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "temperature")
    private String temperature;

    @Column(name = "moisture")
    private String moisture;

    @Column(name = "ec")
    private String EC;

    @Column(name = "salinity")
    private String salinity;

    @Column(name = "tds")
    private String TDS;

    @Column(name = "nitrogen")
    private String nitrogen;

    @Column(name = "phosphorus")
    private String phosphorus;

    @Column(name = "potassium")
    private String potassium;

    @Column(name = "layer_level")
    private String layerLevel;

    @JsonBackReference
    //@JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "deviceID", referencedColumnName = "device_id")
    private Set <DataResponse> dataResponse = new HashSet<>();

    @Column(name = "device_id")
    private String deviceID;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    // Getters and Setters

}
