package com.quy.highconcurrency_ticket_system.repository;

import com.quy.highconcurrency_ticket_system.model.EventSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventSessionRepository extends JpaRepository<EventSession, Long> {
    List<EventSession> findEventSessionByEvent_Id(Long eventId);
}
