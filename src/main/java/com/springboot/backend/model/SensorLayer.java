package com.springboot.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_sensor_layer")
public class SensorLayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_layer_id", nullable = false)
    long id;

/*    @JsonManagedReference
    @OneToMany(targetEntity=SensorData.class, mappedBy="sensorLayer",fetch = FetchType.EAGER)
    private Set<SensorData> sensorData = new HashSet<>();*/

    @JsonBackReference
    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "response_id")
    private DataResponse dataResponse;
}
