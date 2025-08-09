package com.springboot.backend.repository;

import com.springboot.backend.model.Device;
import com.springboot.backend.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FieldRepository extends JpaRepository <Field, Long> {

    Field findFieldById(long id);
    @Query("from Field e where (e.user.id =:user_id and e.delete_status=false)")
    Field findByUserId(@Param("user_id") Long user_id);

    @Transactional
    @Modifying
    @Query("update Field set delete_status = 1 where id =:id")
    void markFieldAsDeleted(@Param("id") Long id);

}
