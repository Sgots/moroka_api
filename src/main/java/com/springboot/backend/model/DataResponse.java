package com.springboot.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Table(name = "tbl_data_response")
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataResponse {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "device_name" )
  private String deviceName;

  @Column(name = "device_id")
  private long deviceID;

  @JsonIgnore
  @OneToOne
  @JoinColumn(name = "device_id", insertable = false, updatable = false)
  private Device device;

  @Column(name = "device_type_id")
  private int deviceTypeID;

  @Column(name = "device_sub_type_id")
  private int deviceSubTypeID;

  @Column(name = "battery_level")
  private int batteryLevel;

  @Column(name = "device_state")
  private int deviceState;

  @Column(name = "control_state")
  private int controlState;


  //@JsonIgnore
  @ManyToOne
  @JoinColumn(name = "field_id")
  private Field field;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "area_id")
  private Area area;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "patch_id")
  private Patch patchID;

  @Column(name = "command")
  private String command;
 /* @Column(name = "layer_level")
  private int layerLevel;*/

  @Column(name = "time_stamp")
  private String timeStamp;

  @JsonIgnore
  @JsonManagedReference
  @ManyToMany(targetEntity = SensorData.class, mappedBy = "dataResponse", fetch = FetchType.LAZY)
  private Set<SensorData> sensorData = new HashSet<>();

  @JsonIgnore
  @JsonManagedReference
  @ManyToMany(targetEntity = FlowMeter.class, mappedBy = "dataResponse", fetch = FetchType.LAZY)
  private Set<FlowMeter> flowMeter = new HashSet<>();

  // Getters and Setters
}
