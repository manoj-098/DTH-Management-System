package com.example.dthMgmtSys.dto;

import com.example.dthMgmtSys.model.enums.PaymentStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO {

  private Integer id;
  private double amount;
  private LocalDateTime dateTime;
  private PaymentStatus paymentStatus;
  private Integer subscriptionId;
}
