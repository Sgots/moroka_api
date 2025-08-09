package com.springboot.backend.repository;

import com.springboot.backend.model.OneTimePassword;
import org.springframework.data.repository.CrudRepository;

public interface OneTimePasswordRepository extends CrudRepository<OneTimePassword, Long> {

}
