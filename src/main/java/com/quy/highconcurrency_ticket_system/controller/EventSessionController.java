package com.quy.highconcurrency_ticket_system.controller;

import com.quy.highconcurrency_ticket_system.dto.request.EventSessionRequest;
import com.quy.highconcurrency_ticket_system.dto.request.EventSessionUpdateRq;
import com.quy.highconcurrency_ticket_system.dto.response.APIResponse;
import com.quy.highconcurrency_ticket_system.dto.response.EventSessionResponse;
import com.quy.highconcurrency_ticket_system.service.EventSessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/admin/sessions")
@Tag(name = "Event Session", description = "Event Session Management APIs")
public class EventSessionController {
    private final EventSessionService eventSessionService;

    public EventSessionController(EventSessionService eventSessionService) {
        this.eventSessionService = eventSessionService;
    }

    @Operation(summary = "Get all event sessions", description = "Retrieve a list of all event sessions")
    @ApiResponse(responseCode = "200", description = "Event sessions retrieved successfully")
    @GetMapping
    public ResponseEntity<APIResponse<List<EventSessionResponse>>> index(){
        APIResponse<List<EventSessionResponse>> response = new APIResponse<>(HttpStatus.OK.value()
                , "Event sessions retrieved successfully", eventSessionService.index(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get event session by ID", description = "Retrieve an event session by its ID")
    @ApiResponse(responseCode = "200", description = "Event session retrieved successfully")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<EventSessionResponse>> findById(@PathVariable Long id){
        APIResponse<EventSessionResponse> response = new APIResponse<>(HttpStatus.OK.value(), "Event session retrieved successfully"
                , eventSessionService.findById(id), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Create an event session", description = "Create a new event session for a specific event")
    @ApiResponse(responseCode = "201", description = "Event session created successfully")
    @PostMapping("/{eventId}")
    public ResponseEntity<APIResponse<EventSessionResponse>> create(@PathVariable Long eventId ,@Valid @RequestBody EventSessionRequest request){
        APIResponse<EventSessionResponse> response = new APIResponse<>(HttpStatus.CREATED.value(), "Event session created successfully"
                , eventSessionService.create(eventId, request), null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an event session", description = "Update an existing event session by its ID")
    @ApiResponse(responseCode = "200", description = "Event session updated successfully")
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<EventSessionResponse>> update(@PathVariable Long id, @Valid @RequestBody EventSessionUpdateRq request){
        APIResponse<EventSessionResponse> response = new APIResponse<>(HttpStatus.OK.value(), "Event session updated successfully"
                , eventSessionService.update(id, request), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Delete an event session", description = "Delete an event session by its ID")
    @ApiResponse(responseCode = "204", description = "Event session deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Object>> delete(@PathVariable Long id){
        eventSessionService.delete(id);
        APIResponse<Object> response = new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Event session cancelled"
                , null, null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
