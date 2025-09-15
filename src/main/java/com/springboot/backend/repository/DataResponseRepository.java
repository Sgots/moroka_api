package com.springboot.backend.repository;

import com.springboot.backend.model.DataResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DataResponseRepository extends JpaRepository<DataResponse, Long> {

    @Query(value = """
        SELECT *
        FROM tbl_data_response d
        WHERE d.device_id = :id
        ORDER BY id DESC
        LIMIT 1
    """, nativeQuery = true)
    DataResponse findLatestIdByDeviceId(long id);

    @Query(value="select * from tbl_data_response d where d.device_id = :id order by id desc limit 1", nativeQuery=true)
    List<DataResponse> getDevicesResponse(long id);

    @Query("from DataResponse d where d.field.id = :id order by d.id desc limit 1")
    DataResponse findFieldByID(long id);
}
