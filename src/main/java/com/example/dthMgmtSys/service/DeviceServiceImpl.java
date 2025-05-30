package com.example.dthMgmtSys.service;

import com.example.dthMgmtSys.dto.ApiResponse;
import com.example.dthMgmtSys.dto.DeviceRequestDTO;
import com.example.dthMgmtSys.dto.DeviceResponseDTO;
import com.example.dthMgmtSys.model.Device;
import com.example.dthMgmtSys.model.User;
import com.example.dthMgmtSys.model.enums.Role;
import com.example.dthMgmtSys.repository.DeviceRepository;
import com.example.dthMgmtSys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<ApiResponse> getDevicesBasedOnRole(String userName) {
        // INFO: Returned response using a custom wrapper class ApiResponse
        // TODO: Yet to do the above for other methods as well
        Role userRole = userRepository.findRoleByUserName(userName);
        if (Objects.equals(userRole.toString(), "ADMIN")) {
            // Show all devices sorted by UserId
            List<DeviceResponseDTO> deviceResponseDTOS = convertDeviceToDTO(deviceRepository.findAllDevicesSortedByUserId());
            if (deviceResponseDTOS.isEmpty())
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ApiResponse(
                        "No data found", null, LocalDateTime.now()));
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(
                            "Data fetched successfully", deviceResponseDTOS, LocalDateTime.now()));
        } else if (Objects.equals(userRole.toString(), "USER")) {
            // show devices of that user
            List<DeviceResponseDTO> deviceResponseDTOS = convertDeviceToDTO(deviceRepository.findAllDevicesOfAUser(userName).orElse(List.of()));
            if (deviceResponseDTOS.isEmpty())
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiResponse(
                        "No data found", null, LocalDateTime.now()));
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(
                    "Data fetched successfully", deviceResponseDTOS, LocalDateTime.now()));
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(new ApiResponse(
                        "SERVICE_PROVIDER access is not yet implemented", null, LocalDateTime.now()));
    }

    @Override
    public ResponseEntity<ApiResponse> getDevicesByUserName(String userName) {
        List<DeviceResponseDTO> deviceResponseDTOS = convertDeviceToDTO(deviceRepository.findAllDevicesOfAUser(userName).orElse(List.of()));
        if (deviceResponseDTOS.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                new ApiResponse(
                        "No data found", null, LocalDateTime.now()));
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Data fetched successfully",deviceResponseDTOS, LocalDateTime.now()));
    }

    @Override
    public ResponseEntity<ApiResponse> addDevice(List<DeviceRequestDTO> deviceRequestDTOS, String userName) {
        List<String> resultMessages = new ArrayList<>();
        for (DeviceRequestDTO deviceRequestDTO : deviceRequestDTOS) {
            Device device = new Device();
            device.setDeviceName(deviceRequestDTO.getDeviceName());
            User user = userRepository.findByUserName(userName).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ApiResponse(
                                "No user found with UserName: [" + userName + "]", null, LocalDateTime.now()));
            }
            device.setUser(user);
            deviceRepository.save(device);
            resultMessages.add("Device [" + deviceRequestDTO.getDeviceName() + "] added successfully");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse(
                        resultMessages.toString(), null, LocalDateTime.now()));
    }

    @Override
    public ResponseEntity<ApiResponse> updateDeviceById(Integer deviceId, DeviceRequestDTO deviceRequestDTO, String userName) {
        String realDeviceUserName = deviceRepository.findUserNameByDeviceId(deviceId).orElse(null);
        if (realDeviceUserName == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(
                            "No Device found with Device-Id: [" + deviceId + "]", null, LocalDateTime.now()));
        } else if (!Objects.equals(userRepository.findRoleByUserName(userName).toString(), "ADMIN") &&
                !realDeviceUserName.equalsIgnoreCase(userName)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new ApiResponse(
                            "You cannot access other user's device without an ADMIN privilege", null, LocalDateTime.now())
            );
        }
        Device device = deviceRepository.findById(deviceId).orElse(null);
        device.setDeviceName(deviceRequestDTO.getDeviceName());
        deviceRepository.save(device);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse("Device with id ["+ deviceId +"] updated successfully", null, LocalDateTime.now())
        );
    }

    @Override
    public ResponseEntity<ApiResponse> deleteDevice(Integer deviceId, String userName) {
        String realDeviceUserName = deviceRepository.findUserNameByDeviceId(deviceId).orElse(null);
        if (realDeviceUserName == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                            new ApiResponse(
                                    "No Device found with Device-Id: [" + deviceId + "]", null, LocalDateTime.now())
                    );
        } else if (!Objects.equals(userRepository.findRoleByUserName(userName).toString(), "ADMIN") &&
                !realDeviceUserName.equalsIgnoreCase(userName)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new ApiResponse(
                            "You cannot access other user's device without an ADMIN privilege", null, LocalDateTime.now()
                    )
            );
        }
        Device device = deviceRepository.findById(deviceId).orElse(null);
        deviceRepository.deleteById(deviceId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Device with id [" + deviceId + "] deleted successfully", null, LocalDateTime.now()
                )
        );
    }

    @Override
    public List<DeviceResponseDTO> convertDeviceToDTO(List<Device> devices) {
        // Convert Entity to DTO
        return devices.stream()
                .map(device -> {
                    DeviceResponseDTO dto = new DeviceResponseDTO();
                    dto.setDeviceName(device.getDeviceName());
                    dto.setUserName(device.getUser().getUserName());
                    return dto;
                })
                .toList();
    }

//    @Override
//    public ResponseEntity<?> showAllDevices(Integer userId) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String userName = auth.getName();
//        // TODO: get the userName from auth -> query the Role of that userName ->
//        //      if Role=="ADMIN" -> then proceed
//        //      else             -> get the userId from DB, check with the current method's userId
//        //                              if both are same -> proceed & display the devices associated with that userId
//        //                              else             -> throw Access Denied Exception
//
//        Role userRole = userRepository.findRoleByUserName(userName);
//        List<Device> devices = deviceRepository.findAllDevicesOfAUser(userId);
//        List<DeviceDTO> deviceDTOs = convertDeviceToDTO(devices);
//        if(Objects.equals(userRole.toString(), "ADMIN")){
//            if(deviceDTOs.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(deviceDTOs);
//            return ResponseEntity.status(HttpStatus.OK).body(deviceDTOs);
//
//        } else {
//            Integer loggedInUserId = userRepository.findUserIdByUserName(userName);
//            if(loggedInUserId == userId){
//                if(deviceDTOs.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(deviceDTOs);
//                return ResponseEntity.status(HttpStatus.OK).body(deviceDTOs);
//            } else{
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).
//                        body("You cannot fetch the devices of another user.\n" +
//                                "Your UserId      : ["+ loggedInUserId + "]\n" +
//                                "Requested userId : ["+ userId + "]");
//            }
//        }
//    }

}
