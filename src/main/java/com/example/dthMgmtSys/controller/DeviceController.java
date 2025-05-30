package com.example.dthMgmtSys.controller;

import com.example.dthMgmtSys.dto.ApiResponse;
import com.example.dthMgmtSys.dto.DeviceRequestDTO;
import com.example.dthMgmtSys.service.DeviceServiceImpl;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dthMgmtSys/device")
public class DeviceController {

  @Autowired
  DeviceServiceImpl deviceService;

  @PreAuthorize("hasAnyRole( 'USER', 'ADMIN' )")
  @GetMapping("/devices")
  public ResponseEntity<ApiResponse> getDevicesOfLoggedInUser(Authentication authentication) {
    return deviceService.getDevicesBasedOnRole(authentication.getName());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/device/{userName}")
  public ResponseEntity<ApiResponse> getDevicesOfAnyUser(
      @PathVariable("userName") String userName
  ) {
    return deviceService.getDevicesByUserName(userName);
  }

  @PreAuthorize("hasRole( 'USER')")
  @PostMapping("/devices")
  public ResponseEntity<ApiResponse> addDevices(
      @RequestBody @Valid List<DeviceRequestDTO> deviceRequestDTOs, Authentication authentication) {
    return deviceService.addDevice(deviceRequestDTOs, authentication.getName());
  }

  @PreAuthorize("hasAnyRole( 'USER', 'ADMIN')")
  @PutMapping("/devices/{deviceId}")
  public ResponseEntity<ApiResponse> updateDevice(
      @PathVariable("deviceId") Integer deviceId,
      @RequestBody @Valid DeviceRequestDTO deviceRequestDTO, Authentication authentication
  ) {
    return deviceService.updateDeviceById(deviceId, deviceRequestDTO, authentication.getName());
  }

  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @DeleteMapping("/device/{deviceId}")
  public ResponseEntity<ApiResponse> deleteDeviceById(
      @PathVariable("deviceId") Integer deviceId, Authentication authentication) {
    return deviceService.deleteDevice(deviceId, authentication.getName());
  }

}
