package com.springboot.backend.repository;

import com.springboot.backend.model.DataResponse;
import com.springboot.backend.model.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    List<SensorData> findAllByDeviceID(long device_id);


 /*   @Query("FROM SensorData d WHERE d.dataResponse. = :id" )
    List<SensorData> findAllByDeviceID(long id);*/

    @Query(value="select * from tbl_sensor_data where device_id =? and layer_level=? order by id desc limit 1", nativeQuery=true)
    List<SensorData> getSensorData(String device_id, long layer_level);
    @Query(value = """
        select tbl_sensor_data.*
        from tbl_sensor_data
        join tbl_data_response tdr on tbl_sensor_data.device_id = tdr.device_id
        join tbl_fields tf on tdr.field_id = tf.field_id
        where tf.field_id = ?1
        and tdr.device_id = ?2
        and tbl_sensor_data.layer_level = ?3
        and tf.user_id = ?4""", nativeQuery = true)
    List<SensorData> findBySensorDataByFieldID(Long fieldId, Long deviceId, Integer layerLevel, Long userId);


   /* @Query(value="select * from tbl_sensor_data where field_id =? device_id =? and layer_level=? order by id desc limit 1", nativeQuery=true)
    List<SensorData> getSensorData(long device_id, long layer_level);*/
   @Query(value = "SELECT * FROM tbl_sensor_data ts " +
           "JOIN tbl_devices td ON ts.device_id = td.device_uuid " +
           "WHERE td.fieldID = :fieldId " +
           "AND td.user_id = :userId " +
           "AND td.device_uuid = :deviceUuid " +
           "AND ts.layer_level = :layerLevel " +
           "ORDER BY ts.id DESC LIMIT 1", nativeQuery = true)
   List<SensorData> findSensorData(@Param("fieldId") Long fieldId,
                                   @Param("userId") Long userId,
                                   @Param("deviceUuid") String deviceUuid,
                                   @Param("layerLevel") Integer layerLevel);
}
