package com.springboot.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = ("tbl_notifications"))
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notifications_id",nullable = false)
    private long id;

    private long device_id;

}
