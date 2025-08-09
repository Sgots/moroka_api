package com.springboot.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "tbl_account_log")
public class AccountLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_log_id")
    private long id;

    String email;
    String status;
    @Column(name = "accessed_at", updatable = false)
    private LocalDateTime accessed_at = LocalDateTime.now();

}
