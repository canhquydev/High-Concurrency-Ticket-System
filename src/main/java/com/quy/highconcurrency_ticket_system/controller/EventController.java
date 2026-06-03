package com.quy.highconcurrency_ticket_system.controller;

import com.quy.highconcurrency_ticket_system.dto.request.EventRequest;
import com.quy.highconcurrency_ticket_system.dto.request.EventUpdateRq;
import com.quy.highconcurrency_ticket_system.dto.response.APIResponse;
import com.quy.highconcurrency_ticket_system.dto.response.EventResponse;
import com.quy.highconcurrency_ticket_system.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<EventResponse>>> index(){
        APIResponse<List<EventResponse>> response = new APIResponse<>(HttpStatus.OK.value(), "Events retrieved successfully"
                , eventService.index(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<APIResponse<EventResponse>> findById(@PathVariable Long eventId){
        APIResponse<EventResponse> response = new APIResponse<>(HttpStatus.OK.value(), "Event retrieved successfully"
                , eventService.findById(eventId), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<APIResponse<EventResponse>> create(@Valid @RequestBody EventRequest request){
        APIResponse<EventResponse> response = new APIResponse<>(HttpStatus.CREATED.value(), "Event created successfully"
                , eventService.create(request), null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<EventResponse>> update(@PathVariable Long id,@Valid @RequestBody EventUpdateRq request){
        APIResponse<EventResponse> response = new APIResponse<>(HttpStatus.OK.value(), "Event updated successfully"
                , eventService.update(id, request), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id){
        eventService.delete(id);
        APIResponse<Object> response = new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Event deleted successfully"
                , null, null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

}
