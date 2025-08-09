package com.springboot.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Collection;

@Data
@Entity
@Table(name = "tbl_device_subtype")
public class DeviceSubType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;


    private long device_sub_type_id;
    private String device_subtype_name;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "device_type_id", referencedColumnName = "device_type_id")
    private DeviceTypes device_type;

    @JsonIgnore
    @OneToMany(mappedBy = "deviceSubType")
    // inverse side: it has a mappedBy attribute, and can't decide how the association is mapped, since the other side already decided it.
    @Fetch(FetchMode.JOIN)
    private Collection<Device> devices;

    private int delete_status = 0;
}
