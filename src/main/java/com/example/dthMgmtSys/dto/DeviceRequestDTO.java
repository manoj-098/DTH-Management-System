package com.example.dthMgmtSys.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRequestDTO {

  @NotBlank(message = "Device Name is required")
  private String deviceName;
}
