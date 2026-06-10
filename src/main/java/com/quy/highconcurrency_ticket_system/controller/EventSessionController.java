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

@RestController
@RequestMapping("/api/v1/admin/sessions")
public class EventSessionController {
    private final EventSessionService eventSessionService;

    public EventSessionController(EventSessionService eventSessionService) {
        this.eventSessionService = eventSessionService;
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<EventSessionResponse>>> index(){
        APIResponse<List<EventSessionResponse>> response = new APIResponse<>(HttpStatus.OK.value()
                , "Event sessions retrieved successfully", eventSessionService.index(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<EventSessionResponse>> findById(@PathVariable Long id){
        APIResponse<EventSessionResponse> response = new APIResponse<>(HttpStatus.OK.value(), "Event session retrieved successfully"
                , eventSessionService.findById(id), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<APIResponse<EventSessionResponse>> create(@PathVariable Long eventId ,@Valid @RequestBody EventSessionRequest request){
        APIResponse<EventSessionResponse> response = new APIResponse<>(HttpStatus.CREATED.value(), "Event session created successfully"
                , eventSessionService.create(eventId, request), null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<EventSessionResponse>> update(@PathVariable Long id, @Valid @RequestBody EventSessionUpdateRq request){
        APIResponse<EventSessionResponse> response = new APIResponse<>(HttpStatus.OK.value(), "Event session updated successfully"
                , eventSessionService.update(id, request), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Object>> delete(@PathVariable Long id){
        eventSessionService.delete(id);
        APIResponse<Object> response = new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Event session cancelled"
                , null, null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
