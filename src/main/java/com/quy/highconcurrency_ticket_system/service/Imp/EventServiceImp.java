package com.quy.highconcurrency_ticket_system.service.Imp;

import com.quy.highconcurrency_ticket_system.dto.request.EventRequest;
import com.quy.highconcurrency_ticket_system.dto.request.EventUpdateRq;
import com.quy.highconcurrency_ticket_system.dto.response.EventResponse;
import com.quy.highconcurrency_ticket_system.dto.response.EventSessionResponse;
import com.quy.highconcurrency_ticket_system.dto.response.TicketResponse;
import com.quy.highconcurrency_ticket_system.enums.EventStatus;
import com.quy.highconcurrency_ticket_system.exception.ResourceNotFoundException;
import com.quy.highconcurrency_ticket_system.model.Event;
import com.quy.highconcurrency_ticket_system.model.Ticket;
import com.quy.highconcurrency_ticket_system.repository.EventRepository;
import com.quy.highconcurrency_ticket_system.repository.EventSessionRepository;
import com.quy.highconcurrency_ticket_system.repository.TicketRepository;
import com.quy.highconcurrency_ticket_system.service.EventService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class EventServiceImp implements EventService {

    private final EventRepository eventRepository;
    private final EventSessionRepository eventSessionRepository;
    private final TicketRepository ticketRepository;

    public EventServiceImp(EventRepository eventRepository, EventSessionRepository eventSessionRepository, TicketRepository ticketRepository) {
        this.eventRepository = eventRepository;
        this.eventSessionRepository = eventSessionRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public EventResponse create(EventRequest request) {
        Event event = Event.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(EventStatus.valueOf(request.getStatus().toUpperCase()))
                .build();
        eventRepository.save(event);
        return new EventResponse(event);
    }

    @Override
    public List<EventResponse> index() {
        List<Event> events = eventRepository.findAll();
        List<EventResponse> eventResponses = new ArrayList<>();
        for(Event e: events){
            eventResponses.add(new EventResponse(e));
        }
        return eventResponses;
    }

    @Override
    @Transactional()
    public EventResponse findById(Long eventId) {
        Optional<Event> event = eventRepository.findByIdWithDetails(eventId);
        if(event.isEmpty()){
            throw new ResourceNotFoundException("Event", "Id", eventId);
        }
        return new EventResponse(event.get());
    }

    @Override
    public EventResponse update(Long id, EventUpdateRq request) {
        Optional<Event> event = eventRepository.findById(id);
        if(event.isEmpty()){
            throw new ResourceNotFoundException("Event", "id", id);
        }
        Event eventUpdate = event.get();
        if(request.getName() != null){
            eventUpdate.setName(request.getName());
        }
        if(request.getDescription() != null){
            eventUpdate.setDescription(request.getDescription());
        }
        if(request.getStatus() != null){
            eventUpdate.setStatus(EventStatus.valueOf(request.getStatus().toUpperCase()));
        }
        eventRepository.save(eventUpdate);
        return new EventResponse(eventUpdate);
    }

    @Override
    public void delete(Long id) {
        Optional<Event> event = eventRepository.findById(id);
        if(event.isEmpty()){
            throw new ResourceNotFoundException("Event", "id", id);
        }
        Event eventDelete = event.get();
        eventDelete.setDeleted(true);
        eventRepository.save(eventDelete);
    }
}
