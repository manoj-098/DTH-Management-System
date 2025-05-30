package com.example.dthMgmtSys.controller;

import com.example.dthMgmtSys.dto.ApiResponse;
import com.example.dthMgmtSys.dto.SupportTicketDTO;
import com.example.dthMgmtSys.model.enums.TicketStatus;
import com.example.dthMgmtSys.service.SupportTicketImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dthMgmtSys/ticket")
public class SupportTicketController {

  @Autowired
  SupportTicketImpl supportTicket;

  @GetMapping("/tickets")
  public ResponseEntity<ApiResponse> getAllTickets(Authentication authentication) {
    return supportTicket.getAllTicketsBasedOnUserRole(authentication.getName());
  }

  @GetMapping("/ticket/{ticketId}")
  public ResponseEntity<ApiResponse> getTicketById(
      @PathVariable("ticketId") Integer ticketId,
      Authentication authentication
  ) {
    return supportTicket.getTicketById(ticketId, authentication.getName());
  }

  @PreAuthorize("hasRole('USER')")
  @PostMapping("/ticket")
  public ResponseEntity<ApiResponse> raiseTicket(
      SupportTicketDTO supportTicketDTO,
      Authentication authentication
  ) {
    return supportTicket.raiseTicket(supportTicketDTO, authentication.getName());
  }

  @PreAuthorize("hasAnyRole('SERVICE_PROVIDER', 'ADMIN')")
  @PutMapping("ticketStatus/{ticketId}")
  public ResponseEntity<ApiResponse> updateTicketStatus(
      @PathVariable("ticketId") Integer ticketId,
      TicketStatus updatedTicketStatus, Authentication authentication
  ) {
    return supportTicket.updateTicketStatus(
        ticketId, updatedTicketStatus, authentication.getName());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/ticket/{ticketId}")
  public ResponseEntity<ApiResponse> updateTicket(
      @PathVariable("ticketId") Integer ticketId,
      SupportTicketDTO supportTicketDTO
  ) {
    return supportTicket.updateTicket(ticketId, supportTicketDTO);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("ticket/{ticketId}")
  public ResponseEntity<ApiResponse> deleteTicket(@PathVariable("ticketId") Integer ticketId) {
    return supportTicket.deleteTicketById(ticketId);
  }

}
