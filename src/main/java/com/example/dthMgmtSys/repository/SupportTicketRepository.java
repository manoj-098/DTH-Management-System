package com.example.dthMgmtSys.repository;

import com.example.dthMgmtSys.model.SupportTicket;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Integer> {

    @Query("SELECT S FROM SupportTicket S WHERE LOWER(S.user.userName) = LOWER(:userName)")
    Optional<List<SupportTicket>> findTicketsByUserName(@Param("userName") String userName);

    @Query("SELECT S FROM SupportTicket S WHERE LOWER(S.assignedTo) = LOWER(:userName)")
    Optional<List<SupportTicket>> findTicketsByServiceProvider(@Param("userName") String userName);
}
