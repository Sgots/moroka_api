package com.springboot.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "tbl_topics")
public class Topics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    long id;
    private String controlTopic;
    private String dataTopic;
    private String notificationTopic;
    private String deviceStateTopic;
    private String configurationReadyTopic;
    private String irrigationOverrideTopic;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false, referencedColumnName = "device_id")
    private MasterDevice masterDevice;

}
