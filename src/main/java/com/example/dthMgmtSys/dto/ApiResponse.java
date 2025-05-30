package com.example.dthMgmtSys.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {

  private String message;
  private Object data;
  private LocalDateTime timestamp;
}
