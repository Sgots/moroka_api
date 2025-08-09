package com.springboot.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = ("tbl_user_permissions"))
public class Permissions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PermissionList permissionList;

}
