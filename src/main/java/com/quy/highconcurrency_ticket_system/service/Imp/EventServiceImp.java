package com.quy.highconcurrency_ticket_system.service.Imp;

import com.quy.highconcurrency_ticket_system.dto.request.EventRequest;
import com.quy.highconcurrency_ticket_system.dto.request.EventUpdateRq;
import com.quy.highconcurrency_ticket_system.dto.response.EventResponse;
import com.quy.highconcurrency_ticket_system.enums.EventStatus;
import com.quy.highconcurrency_ticket_system.exception.ResourceNotFoundException;
import com.quy.highconcurrency_ticket_system.model.Event;
import com.quy.highconcurrency_ticket_system.repository.EventRepository;
import com.quy.highconcurrency_ticket_system.service.EventService;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class EventServiceImp implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImp(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    @CacheEvict(value = "eventsOngoing", allEntries = true)
    public EventResponse create(EventRequest request) {
        Event event = Event.builder()
                .name(request.getName())
                .location(request.getLocation())
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
    @Cacheable(value = "eventsOngoing")
    public List<EventResponse> eventsOngoing() {
        List<Event> eventList = eventRepository.findByStatus(EventStatus.ONGOING);
        if(eventList == null){
            throw new ResourceNotFoundException("Event", "Status", "ONGOING");
        }
        List<EventResponse> eventResponses = new ArrayList<>();
        for(Event event: eventList){
            eventResponses.add(new EventResponse(event));
        }
        return eventResponses;
    }

    @Override
    @CacheEvict(value = "eventsOngoing", allEntries = true)
    public EventResponse update(Long id, EventUpdateRq request) {
        Optional<Event> event = eventRepository.findById(id);
        if(event.isEmpty()){
            throw new ResourceNotFoundException("Event", "id", id);
        }
        Event eventUpdate = event.get();
        if(StringUtils.hasText(request.getName())){
            eventUpdate.setName(request.getName());
        }
        if(StringUtils.hasText(request.getDescription())){
            eventUpdate.setDescription(request.getDescription());
        }
        if(StringUtils.hasText(request.getLocation())){
            eventUpdate.setDescription(request.getLocation());
        }
        if(StringUtils.hasText(request.getStatus())){
            eventUpdate.setStatus(EventStatus.valueOf(request.getStatus().toUpperCase()));
        }
        eventRepository.save(eventUpdate);
        return new EventResponse(eventUpdate);
    }

    @Override
    @CacheEvict(value = "eventsOngoing", allEntries = true)
    public void delete(Long id) {
        Optional<Event> event = eventRepository.findById(id);
        if(event.isEmpty()){
            throw new ResourceNotFoundException("Event", "id", id);
        }
        Event eventDelete = event.get();
        eventDelete.setStatus(EventStatus.FINISHED);
        eventRepository.save(eventDelete);
    }
}
