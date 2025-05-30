package com.example.dthMgmtSys.service;

import com.example.dthMgmtSys.dto.ApiResponse;
import com.example.dthMgmtSys.dto.DeviceRequestDTO;
import com.example.dthMgmtSys.dto.DeviceResponseDTO;
import com.example.dthMgmtSys.model.Device;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface DeviceService {

  ResponseEntity<ApiResponse> getDevicesBasedOnRole(String userName);

  ResponseEntity<ApiResponse> getDevicesByUserName(String userName);

  ResponseEntity<ApiResponse> addDevice(List<DeviceRequestDTO> deviceRequestDTOS, String userName);

  ResponseEntity<ApiResponse> updateDeviceById(
      Integer deviceId, DeviceRequestDTO deviceRequestDTO, String userName);

  ResponseEntity<ApiResponse> deleteDevice(Integer deviceId, String userName);

  List<DeviceResponseDTO> convertDeviceToDTO(List<Device> devices);
}
