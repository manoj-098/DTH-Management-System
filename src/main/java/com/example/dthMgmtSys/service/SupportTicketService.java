package com.example.dthMgmtSys.service;

import com.example.dthMgmtSys.dto.ApiResponse;
import com.example.dthMgmtSys.dto.SupportTicketDTO;
import com.example.dthMgmtSys.model.SupportTicket;
import com.example.dthMgmtSys.model.enums.TicketStatus;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface SupportTicketService {

  ResponseEntity<ApiResponse> getAllTicketsBasedOnUserRole(String userName);

  ResponseEntity<ApiResponse> getTicketById(Integer ticketId, String userName);

  ResponseEntity<ApiResponse> raiseTicket(SupportTicketDTO supportTicketDTO, String userName);

  ResponseEntity<ApiResponse> updateTicketStatus(
      Integer ticketId, TicketStatus updatedTicketStatus, String userName);

  ResponseEntity<ApiResponse> updateTicket(Integer ticketId, SupportTicketDTO supportTicketDTO);

  ResponseEntity<ApiResponse> deleteTicketById(Integer ticketId);

  List<SupportTicketDTO> convertTicketsToDTO(List<SupportTicket> supportTickets);

  SupportTicketDTO convertTicketToDTO(SupportTicket supportTicket);

}
