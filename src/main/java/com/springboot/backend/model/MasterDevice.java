package com.springboot.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.backend.MQTT.MqttServerConfig;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = ("tbl_master"), uniqueConstraints = {
        @UniqueConstraint(columnNames = {"device_serial_number"}
        ),
        @UniqueConstraint(columnNames = {"device_uuid"})
})

public class MasterDevice {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id", nullable = false)
    private long id;
    private long device_type_id;
    private long device_sub_type_id;
    private long field_id;
    @Column(name = "device_uuid")
    private long device_uuid;
    @Column(name = "device_serial_number")
    private String device_serial_number;
    @Column(name = "device_name")
    private String device_name;
    @Column(name = "mac_address")
    private String mac_address;
    @Column(name = "command")
    private String command;
    private int delete_status = 0;
    private int device_status = 0;
    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "deviceResponseID")
    private DataResponse dataResponse;


    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            mappedBy = "masterDevice")
    private EncryptionSettings encryptionSettings;
    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            mappedBy = "masterDevice")
    private Topics topics;

    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            mappedBy = "masterDevice")
    private MqttServerConfig mqttServerConfig;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany()
    private List<Device> attachedDevices;
    @OneToMany
    private List<Area> attachedAreas;
    @OneToMany
    private List<Patch> attachedPatches;

}
