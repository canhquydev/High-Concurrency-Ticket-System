package com.quy.highconcurrency_ticket_system.service;

import com.quy.highconcurrency_ticket_system.dto.request.EventSessionRequest;
import com.quy.highconcurrency_ticket_system.dto.request.EventSessionUpdateRq;
import com.quy.highconcurrency_ticket_system.dto.response.EventSessionResponse;

import java.util.List;

public interface EventSessionService {
    EventSessionResponse create(Long eventId, EventSessionRequest request);
    List<EventSessionResponse> index();
    EventSessionResponse findById(Long id);
    EventSessionResponse update(Long id, EventSessionUpdateRq request);
    void delete(Long id);
}
