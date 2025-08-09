package com.springboot.backend.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddDeviceRequest implements Serializable {

    private long id;
    private String device_name;
    private long device_subtype_id;
    private long device_type_id;
    private long deviceID;
    private long device_uuid;
    private String device_mac_address;
    private String device_serial_no;
    private long fieldID, areaID,patchID;

    private String control_topic,data_topic,
            notification_topic,deviceState_topic,configurationReady_topic;
    private int delete_status;
    private long user_id;
}
