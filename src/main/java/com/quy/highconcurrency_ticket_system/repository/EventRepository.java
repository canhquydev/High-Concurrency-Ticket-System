package com.quy.highconcurrency_ticket_system.repository;

import com.quy.highconcurrency_ticket_system.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
