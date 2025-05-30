package com.example.dthMgmtSys.dto;

import com.example.dthMgmtSys.model.enums.SubscriptionStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionResponseDTO {

  private Integer id;
  private LocalDate startDate;
  private LocalDate endDate;
  private SubscriptionStatus subscriptionStatus;
  private Integer deviceId;
  private String userName;
  private Integer planId;
}
