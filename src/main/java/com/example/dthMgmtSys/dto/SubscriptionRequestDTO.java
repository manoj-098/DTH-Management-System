package com.example.dthMgmtSys.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionRequestDTO {

  @NotNull(message = "Plan-ID is required")
  @Min(value = 1, message = "Plan-ID must be greater than 0")
  private Integer planId;

  @NotNull(message = "Device-ID is required")
  @Min(value = 1, message = "Device-ID must be greater than 0")
  private Integer deviceId;
}
