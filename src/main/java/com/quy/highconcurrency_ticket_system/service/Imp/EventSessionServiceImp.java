package com.quy.highconcurrency_ticket_system.service.Imp;

import com.quy.highconcurrency_ticket_system.dto.request.EventSessionRequest;
import com.quy.highconcurrency_ticket_system.dto.request.EventSessionUpdateRq;
import com.quy.highconcurrency_ticket_system.dto.response.EventSessionResponse;
import com.quy.highconcurrency_ticket_system.dto.response.TicketResponse;
import com.quy.highconcurrency_ticket_system.enums.SessionStatus;
import com.quy.highconcurrency_ticket_system.exception.ResourceNotFoundException;
import com.quy.highconcurrency_ticket_system.model.Event;
import com.quy.highconcurrency_ticket_system.model.EventSession;
import com.quy.highconcurrency_ticket_system.model.Ticket;
import com.quy.highconcurrency_ticket_system.repository.EventRepository;
import com.quy.highconcurrency_ticket_system.repository.EventSessionRepository;
import com.quy.highconcurrency_ticket_system.repository.TicketRepository;
import com.quy.highconcurrency_ticket_system.service.EventSessionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventSessionServiceImp implements EventSessionService {

    private final EventSessionRepository eventSessionRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    public EventSessionServiceImp(EventSessionRepository eventSessionRepository, EventRepository eventRepository, TicketRepository ticketRepository) {
        this.eventSessionRepository = eventSessionRepository;
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    @Transactional
    public EventSessionResponse create(Long eventId, EventSessionRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(()-> new ResourceNotFoundException("Event", "Id", eventId));
        EventSession eventSession = EventSession.builder()
                .event(event)
                .location(request.getLocation())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(SessionStatus.valueOf(request.getStatus()))
                .build();
        eventSessionRepository.save(eventSession);
        return new EventSessionResponse(eventSession);
    }

    @Override
    public List<EventSessionResponse> index() {
        List<EventSession> eventSessions = eventSessionRepository.findAll();
        List<EventSessionResponse> responses = new ArrayList<>();
        for(EventSession e: eventSessions){
            responses.add(new EventSessionResponse(e));
        }
        return responses;
    }

    @Override
    public EventSessionResponse findById(Long eventSessionId) {
        Optional<EventSession> eventSession = eventSessionRepository.findById(eventSessionId);
        if(eventSession.isEmpty()){
            throw new ResourceNotFoundException("Event Session", "id", eventSessionId);
        }
        EventSessionResponse session = new EventSessionResponse(eventSession.get());
        List<Ticket> tickets = ticketRepository.findTicketByEventSession_Id(eventSessionId);
        List<TicketResponse> ticketResponses = new ArrayList<>();
        for(Ticket ticket: tickets){
            ticketResponses.add(new TicketResponse(ticket));
        }
        session.setTicketList(ticketResponses);
        return session;
    }

    @Override
    public EventSessionResponse update(Long id, EventSessionUpdateRq request) {
        Optional<EventSession> eventSession = eventSessionRepository.findById(id);
        if(eventSession.isEmpty()){
            throw new ResourceNotFoundException("Event Session", "id", id);
        }
        EventSession eventSessionUpdate = eventSession.get();
        if(request.getLocation() != null){
            eventSessionUpdate.setLocation(request.getLocation());
        }
        if(request.getStartTime() != null){
            eventSessionUpdate.setStartTime(request.getStartTime());
        }
        if(request.getEndTime() != null){
            eventSessionUpdate.setEndTime(request.getEndTime());
        }
        if(request.getStatus() != null){
            eventSessionUpdate.setStatus(SessionStatus.valueOf(request.getStatus()));
        }
        eventSessionRepository.save(eventSessionUpdate);
        return new EventSessionResponse(eventSessionUpdate);
    }

    @Override
    public void delete(Long id) {
        Optional<EventSession> eventSession = eventSessionRepository.findById(id);
        if(eventSession.isEmpty()){
            throw new ResourceNotFoundException("Event Session", "id", id);
        }
        EventSession eventSessionDelete = eventSession.get();
        eventSessionDelete.setStatus(SessionStatus.CANCELLED);
        eventSessionRepository.save(eventSessionDelete);
    }
}
