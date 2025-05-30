package com.example.dthMgmtSys.service;

import com.example.dthMgmtSys.dto.ApiResponse;
import com.example.dthMgmtSys.dto.SupportTicketDTO;
import com.example.dthMgmtSys.model.SupportTicket;
import com.example.dthMgmtSys.model.User;
import com.example.dthMgmtSys.model.enums.Role;
import com.example.dthMgmtSys.model.enums.TicketStatus;
import com.example.dthMgmtSys.repository.SupportTicketRepository;
import com.example.dthMgmtSys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportTicketImpl implements SupportTicketService {

    private final SupportTicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<ApiResponse> getAllTicketsBasedOnUserRole(String userName){
        User user = userRepository.findByUserName(userName).orElse(null);

        if(user.getRole() == Role.ADMIN){
            List<SupportTicket> supportTickets = ticketRepository.findAll();
            List<SupportTicketDTO> supportTicketDTOS = convertTicketsToDTO(supportTickets);
            if(supportTicketDTOS == null)
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ApiResponse(
                                "No Data found", null, LocalDateTime.now()
                        )
                );

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse(
                            "Data fetched successfully", supportTicketDTOS, LocalDateTime.now()
                    )
            );
        } else if(user.getRole() == Role.USER){
            List<SupportTicket> supportTickets = ticketRepository.findTicketsByUserName(userName).orElse(null);
            List<SupportTicketDTO> supportTicketDTOS = convertTicketsToDTO(supportTickets);
            if(supportTicketDTOS == null)
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ApiResponse(
                                "No Data found", null, LocalDateTime.now()
                        )
                );

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse(
                            "Data fetched successfully", supportTicketDTOS, LocalDateTime.now()
                    )
            );
        } else {
            List<SupportTicket> supportTickets = ticketRepository.findTicketsByServiceProvider(userName).orElse(null);
            List<SupportTicketDTO> supportTicketDTOS = convertTicketsToDTO(supportTickets);
            if(supportTicketDTOS == null)
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ApiResponse(
                                "No Data found", null, LocalDateTime.now()
                        )
                );

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse(
                            "Data fetched successfully", supportTicketDTOS, LocalDateTime.now()
                    )
            );
        }

    }

    @Override
    public ResponseEntity<ApiResponse> getTicketById(Integer ticketId, String userName){
        User user = userRepository.findByUserName(userName).orElse(null);
        SupportTicket supportTicket = ticketRepository.findById(ticketId).orElse(null);
        SupportTicketDTO supportTicketDTO = convertTicketToDTO(supportTicket);

        if(user.getRole() == Role.USER){
            if(supportTicket!=null && supportTicket.getUser().getUserName()!=userName)
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        new ApiResponse(
                                "You cannot access other user's data", null, LocalDateTime.now()
                        )
                );
        } else if(user.getRole() == Role.SERVICE_PROVIDER) {
            if(supportTicket!=null && supportTicket.getAssignedTo()!=userName)
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        new ApiResponse(
                                "You cannot access other user's data", null, LocalDateTime.now()
                        )
                );

        }

        if(supportTicketDTO == null)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse(
                            "No Data found", null, LocalDateTime.now()
                    )
            );

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Data fetched successfully", supportTicketDTO, LocalDateTime.now()
                )
        );

    }

    @Override
    public ResponseEntity<ApiResponse> raiseTicket(SupportTicketDTO supportTicketDTO, String userName){
        User user = userRepository.findByUserName(userName).orElse(null);
        SupportTicket supportTicket = new SupportTicket();
        supportTicket.setUser(user);
        supportTicket.setTicketStatus(TicketStatus.OPEN);
        supportTicket.setDescription(supportTicketDTO.getDescription());
        supportTicket.setAssignedTo(supportTicketDTO.getAssignedTo());
        ticketRepository.save(supportTicket);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse(
                        "Ticket created successfully with Ticket-Id ["+supportTicket.getId()+"]",
                        null, LocalDateTime.now()
                )
        );
    }

    @Override
    public ResponseEntity<ApiResponse> updateTicketStatus(Integer ticketId, TicketStatus updatedTicketStatus, String userName){
        User user = userRepository.findByUserName(userName).orElse(null);
        SupportTicket supportTicket = ticketRepository.findById(ticketId).orElse(null);

        if(user.getRole() == Role.SERVICE_PROVIDER){
            if(supportTicket.getAssignedTo()!=userName){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        new ApiResponse(
                                "You cannot alter the Ticket Status of tickets that are not assigned to you", null, LocalDateTime.now()
                        )
                );
            }
        }
        supportTicket.setTicketStatus(updatedTicketStatus);
        ticketRepository.save(supportTicket);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Ticket status updated successfully with Ticket-Id ["+supportTicket.getId()+"]",
                        null, LocalDateTime.now()
                )
        );

    }

    @Override
    public ResponseEntity<ApiResponse> updateTicket(Integer ticketId, SupportTicketDTO supportTicketDTO){
        SupportTicket supportTicket = ticketRepository.findById(ticketId).orElse(null);
        if(supportTicket == null)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse(
                            "No data available", null, LocalDateTime.now()
                    )
            );

        supportTicket.setTicketStatus(supportTicketDTO.getTicketStatus());
        supportTicket.setDescription(supportTicketDTO.getDescription());
        supportTicket.setAssignedTo(supportTicketDTO.getAssignedTo());
        ticketRepository.save(supportTicket);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Ticket with Ticket-Id ["+supportTicket.getId()+"]  updated successfully",
                        null, LocalDateTime.now()
                )
        );

    }

    @Override
    public ResponseEntity<ApiResponse> deleteTicketById(Integer ticketId){
        SupportTicket supportTicket = ticketRepository.findById(ticketId).orElse(null);
        if(supportTicket == null)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse(
                            "No data available", null, LocalDateTime.now()
                    )
            );

        ticketRepository.deleteById(ticketId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "Ticket with Ticket-Id ["+ticketId+"]  deleted successfully", null, LocalDateTime.now()
                )
        );
    }

    @Override
    public List<SupportTicketDTO> convertTicketsToDTO(List<SupportTicket> supportTickets){
        if(supportTickets == null)
            return Collections.emptyList();

        return supportTickets.stream().
                map( supportTicket -> {
                    SupportTicketDTO dto = new SupportTicketDTO();
                    dto.setTicketStatus(supportTicket.getTicketStatus());
                    dto.setDescription(supportTicket.getDescription());
                    dto.setAssignedTo(supportTicket.getAssignedTo());
                    return dto;
                }).toList();
    }

    @Override
    public SupportTicketDTO convertTicketToDTO(SupportTicket supportTicket){
        if(supportTicket == null)
            return null;

        SupportTicketDTO dto = new SupportTicketDTO();
        dto.setTicketStatus(supportTicket.getTicketStatus());
        dto.setDescription(supportTicket.getDescription());
        dto.setAssignedTo(supportTicket.getAssignedTo());
        return dto;
    }
}
