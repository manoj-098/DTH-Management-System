package com.example.dthMgmtSys.repository;

import com.example.dthMgmtSys.model.Device;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeviceRepository extends JpaRepository<Device, Integer> {

    @Query("SELECT D FROM Device D WHERE D.user.userName = :userName")
    Optional<List<Device>> findAllDevicesOfAUser(@Param("userName") String userName);

    @Query("SELECT D FROM Device D ORDER BY D.user.id")
    List<Device> findAllDevicesSortedByUserId();

    @Query("SELECT D.user.userName FROM Device D WHERE D.id = :deviceId")
    Optional<String> findUserNameByDeviceId(@Param("deviceId") Integer deviceId);
}
