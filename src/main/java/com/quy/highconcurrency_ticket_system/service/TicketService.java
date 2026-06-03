package com.quy.highconcurrency_ticket_system.service;

import com.quy.highconcurrency_ticket_system.dto.request.TicketRequest;
import com.quy.highconcurrency_ticket_system.dto.request.TicketUpdateRq;
import com.quy.highconcurrency_ticket_system.dto.response.TicketResponse;

import java.util.List;

public interface TicketService {
    TicketResponse create(Long eventSessionId, TicketRequest request);
    List<TicketResponse> index();
    TicketResponse findById(Long id);
    TicketResponse update(Long id, TicketUpdateRq request);
    void delete(Long id);
}
