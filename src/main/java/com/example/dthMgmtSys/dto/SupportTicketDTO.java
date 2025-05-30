package com.example.dthMgmtSys.dto;

import com.example.dthMgmtSys.model.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupportTicketDTO {

  private String description;
  private TicketStatus ticketStatus;
  private String assignedTo;
}
