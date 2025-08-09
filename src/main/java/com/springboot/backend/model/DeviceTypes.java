package com.springboot.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.*;

@Table(name = ("tbl_device_types"))
@Data
@Entity
public class DeviceTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_type_id", nullable = false)
    private long id;

    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "device_type")
    // inverse side: it has a mappedBy attribute, and can't decide how the association is mapped, since the other side already decided it.
    @Fetch(FetchMode.JOIN)
    private Collection <DeviceSubType> deviceSubTypes;

    @JsonIgnore
    @OneToMany(targetEntity=Device.class, mappedBy="deviceTypes",fetch = FetchType.EAGER)
    private Collection<Device> devices = new ArrayList<>();

    private int delete_status = 0;


}
