package com.quy.highconcurrency_ticket_system.repository;

import com.quy.highconcurrency_ticket_system.enums.EventStatus;
import com.quy.highconcurrency_ticket_system.model.Event;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @EntityGraph(attributePaths = {"eventSessionsList", "eventSessionsList.ticketList"})
    @Query("Select e from Event e where e.id = ?1")
    Optional<Event> findByIdWithDetails(Long id);

    List<Event> findByStatus(EventStatus status);
}
