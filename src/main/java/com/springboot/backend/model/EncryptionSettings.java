package com.springboot.backend.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_encryption_settings")
public class EncryptionSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private byte[] encryptionKey;
    private byte[] IV1;
    private byte[] IV2;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false, referencedColumnName = "device_id")
    private MasterDevice masterDevice;
}