package com.quy.highconcurrency_ticket_system.service;

import com.quy.highconcurrency_ticket_system.dto.request.EventRequest;
import com.quy.highconcurrency_ticket_system.dto.request.EventUpdateRq;
import com.quy.highconcurrency_ticket_system.dto.response.EventResponse;

import java.util.List;


public interface EventService {
    EventResponse create(EventRequest request);
    List<EventResponse> index();
    EventResponse findById(Long eventId);
    List<EventResponse> eventsOngoing();
    EventResponse update(Long id, EventUpdateRq request);
    void delete(Long id);
}
