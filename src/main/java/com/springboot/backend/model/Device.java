package com.springboot.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = ("tbl_devices"), uniqueConstraints = {
        @UniqueConstraint(columnNames = {"device_serial_number"}
        ),
        @UniqueConstraint(columnNames = {"device_uuid"})
})

public class Device{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deviceID", nullable = false)
    private long id;
    /*@Column(name = "deviceID", nullable = false)
    private long device_ID;*/
    @Column(name = "device_uuid", nullable = false)
    private long device_uuid;
    @Column(name = "device_serial_number", nullable = false)
    private String device_serial_number;

    @Column(name = "device_name", nullable = false)
    private String device_name;
    @Column(name = "mac_address", nullable = false)
    private String mac_address = "N/A";
    private int delete_status = 0;
    private int attach_status = 0;
    private int device_status = 0;
    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "device_type_id", referencedColumnName = "device_type_id")
    private DeviceTypes deviceTypes;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "deviceResponseID")
    private DataResponse dataResponse;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "device_sub_type_id", referencedColumnName = "id")
    private DeviceSubType deviceSubType;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "fieldID", referencedColumnName = "field_id")
    private Field field;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "areaID", referencedColumnName = "area_id")
    private Area area;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "patchID")
    private Patch patch;

    private String control_topic,data_topic,
            notification_topic,deviceState_topic;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

   /* @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "master_device_id", referencedColumnName = "device_id")
    private MasterDevice master_device_id;*/

}
