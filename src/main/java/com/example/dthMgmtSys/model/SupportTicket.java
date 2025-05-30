package com.example.dthMgmtSys.model;

import com.example.dthMgmtSys.model.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SupportTicket")
public class SupportTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;
    private String assignedTo;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
