package com.springboot.backend.repository;

import com.springboot.backend.model.Area;
import com.springboot.backend.model.Patch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PatchRepository extends JpaRepository<Patch, Long> {

    @Query("FROM Patch p WHERE p.id= :id")
    Patch findPatchByID(@Param("id") long id);
    Patch findPatchById(long id);

    @Query("FROM Patch p WHERE p.area.id= :area_id")
    List<Patch> findPatchByAreaID(@Param("area_id") long field_id);

    @Transactional
    @Modifying
    @Query("update Patch set delete_status = 1 where id =:id")
    void markPatchAsDeleted(@Param("id") long id);

}
