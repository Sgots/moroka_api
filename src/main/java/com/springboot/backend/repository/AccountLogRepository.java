package com.springboot.backend.repository;

import com.springboot.backend.model.AccountLog;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountLogRepository extends JpaRepository<AccountLog, Integer> {


}
