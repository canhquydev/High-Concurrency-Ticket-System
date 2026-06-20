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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/admin/events")
@Tag(name = "Event", description = "Event Management APIs")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "Get all events", description = "Retrieve a list of all events")
    @ApiResponse(responseCode = "200", description = "Events retrieved successfully")
    @GetMapping
    public ResponseEntity<APIResponse<List<EventResponse>>> index(){
        APIResponse<List<EventResponse>> response = new APIResponse<>(HttpStatus.OK.value(), "Events retrieved successfully"
                , eventService.index(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get ongoing events", description = "Retrieve a list of all ongoing events")
    @ApiResponse(responseCode = "200", description = "Ongoing events retrieved successfully")
    @GetMapping("/ongoing")
    public ResponseEntity<APIResponse<List<EventResponse>>> listEventOngoing(){
        APIResponse<List<EventResponse>> response = new APIResponse<>(200, "Events ongoing retrieved successfully"
                , eventService.eventsOngoing(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get event by ID", description = "Retrieve an event by its ID")
    @ApiResponse(responseCode = "200", description = "Event retrieved successfully")
    @GetMapping("/{eventId}")
    public ResponseEntity<APIResponse<EventResponse>> findById(@PathVariable Long eventId){
        APIResponse<EventResponse> response = new APIResponse<>(HttpStatus.OK.value(), "Event retrieved successfully"
                , eventService.findById(eventId), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Create an event", description = "Create a new event")
    @ApiResponse(responseCode = "201", description = "Event created successfully")
    @PostMapping
    public ResponseEntity<APIResponse<EventResponse>> create(@Valid @RequestBody EventRequest request){
        APIResponse<EventResponse> response = new APIResponse<>(HttpStatus.CREATED.value(), "Event created successfully"
                , eventService.create(request), null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an event", description = "Update an existing event by its ID")
    @ApiResponse(responseCode = "200", description = "Event updated successfully")
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<EventResponse>> update(@PathVariable Long id,@Valid @RequestBody EventUpdateRq request){
        APIResponse<EventResponse> response = new APIResponse<>(HttpStatus.OK.value(), "Event updated successfully"
                , eventService.update(id, request), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Delete an event", description = "Delete an event by its ID")
    @ApiResponse(responseCode = "204", description = "Event deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id){
        eventService.delete(id);
        APIResponse<Object> response = new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Event deleted successfully"
                , null, null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

}
