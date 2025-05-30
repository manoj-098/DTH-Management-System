package com.example.dthMgmtSys.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanRequestDTO {

  @NotNull(message = "Validity is required")
  private int validity;

  @NotBlank(message = "Description is required")
  private String description;

  @NotBlank(message = "Price is required")
  private double price;
}
