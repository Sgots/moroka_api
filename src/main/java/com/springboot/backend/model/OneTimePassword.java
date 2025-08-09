package com.springboot.backend.model;

import jakarta.persistence.*;
import lombok.Data;


import java.util.Date;
@Data
@Entity
@Table(name = "tbl_otp")
public class OneTimePassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer oneTimePasswordCode;
    private Date expires;
}
