package com.springboot.backend.repository;

import com.springboot.backend.model.Area;
import com.springboot.backend.model.Field;
import com.springboot.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AreaRepository extends JpaRepository<Area, Integer> {

    Area findAreaById(long id);

 /*   @Modifying
    @Transactional
    Area deleteAreaById(int id);*/

    @Transactional
    @Modifying
    @Query("update Area set delete_status = 1 where id =:id")
    void markAreaAsDeleted(@Param("id") Long id);

    @Query("FROM Area a WHERE a.field.id= :field_id and a.delete_status=0")
    List<Area> findAreabyFiedID(@Param("field_id") long field_id);

    @Query("from Area ta join Field tf on ta.field.id = tf.id where ta.id = :id")
    Field findFieldAreaByAreaId(@Param("id") int id);

    @Query("from Area ta join Field tf on ta.field.id = tf.id where ta.id = :id")
    Area findArea(@Param("id") int id);

}
