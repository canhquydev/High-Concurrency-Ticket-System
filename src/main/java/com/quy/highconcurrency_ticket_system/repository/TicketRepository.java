package com.quy.highconcurrency_ticket_system.repository;

import com.quy.highconcurrency_ticket_system.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findTicketByEventSession_Id(Long eventSessionId);
}
