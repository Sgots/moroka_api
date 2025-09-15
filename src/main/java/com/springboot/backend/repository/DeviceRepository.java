package com.springboot.backend.repository;

import com.springboot.backend.model.DataResponse;
import com.springboot.backend.model.Device;
import com.springboot.backend.model.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    // Device findDeviceById(long id);
    Device findDeviceById(long deviceId);

    @Query("from Device d where d.device_uuid= :uuid ")
    Device findDeviceByUUID(String uuid);


    @Query("from Device d where d.user.id= :id ")
    List <Device> findAllByUserId(long id);

    @Transactional
    @Modifying
    @Query("update Device set delete_status = 1 where id =:id")
    void markDeviceAsDeleted(@Param("id") Long id);

    @Query("SELECT d FROM Device d JOIN DataResponse dr ON d.id = dr.deviceID WHERE d.user.id = :user_id ORDER BY dr.id DESC")
    Device findLatestDeviceByUserId(@Param("user_id") Long user_id);

    @Query(value = "SELECT * FROM tbl_data_response WHERE device_id = ?1 ORDER BY id DESC LIMIT 1", nativeQuery = true)
    DataResponse findLatestDataResponseByDeviceId(Long deviceId);


    @Query("SELECT d, dr FROM Device d " +
            "JOIN DataResponse dr ON d.id = dr.deviceID " +
            "WHERE d.user.id = :userId " +
            "ORDER BY dr.id DESC LIMIT 1")
    List<Object[]> findDevicesWithDataResponseByUserId(@Param("userId") long userId);

    @Query("SELECT d FROM Device d " +
            "JOIN d.field f " +
            "WHERE f.id = :fieldId " +
            "AND f.user.id = :userId " +
            "AND f.delete_status = 0 " +
            "AND d.delete_status = 0")
    List<Device> findDevicesByFieldIdAndUserId(@Param("fieldId") Long fieldId, @Param("userId") Long userId);

    @Query(value = "SELECT * FROM tbl_devices td " +
            "JOIN tbl_sensor_data ts ON ts.device_id = td.device_uuid " +
            "WHERE td.fieldID = ?1 " +
            "AND td.user_id = ?2 " +
            "AND td.device_uuid = ?3 " +
            "AND ts.layer_level = ?4 " +
            "ORDER BY ts.id DESC LIMIT 1", nativeQuery = true)
    List<Device> findDevicesWithSensorData(Long fieldId, Long userId, String deviceUuid, Integer layerLevel);
    @Query(value = """
        SELECT * 
        FROM tbl_devices d
        WHERE d.areaID = :areaId
          AND d.user_id = :userId
          AND d.delete_status = 0
        ORDER BY d.deviceID ASC
        """, nativeQuery = true)
    List<Device> findActiveByAreaAndUser(@Param("areaId") long areaId,
                                         @Param("userId") long userId);

    @Query(value = """
      SELECT * 
      FROM tbl_devices d
      WHERE d.patchID = :patchId
        AND d.user_id = :userId
        AND d.delete_status = 0
      ORDER BY d.deviceID ASC
      """, nativeQuery = true)
    List<Device> findPatchDevices(@Param("patchId") long patchId,
                                  @Param("userId") long userId);

}
