package com.example.dthMgmtSys.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDTO {

  @NotBlank(message = "Amount is required")
  @DecimalMin(value = "0", inclusive = false, message = "Amount must be greater than 0")
  private double amount;

  private LocalDateTime dateTime;

  @NotNull(message = "Subscription-ID is required & cannot be null")
  @Min(value = 1, message = "Subscription-ID must be greater than 0")
  private Integer subscriptionId;
}
