package com.springboot.backend.repository;

import com.springboot.backend.model.DeviceTypes;
import com.springboot.backend.model.EncryptionSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EncryptionSettingsRepository extends JpaRepository<EncryptionSettings, Long> {
}
