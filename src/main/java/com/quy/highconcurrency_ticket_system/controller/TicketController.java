package com.quy.highconcurrency_ticket_system.controller;

import com.quy.highconcurrency_ticket_system.dto.request.TicketRequest;
import com.quy.highconcurrency_ticket_system.dto.request.TicketUpdateRq;
import com.quy.highconcurrency_ticket_system.dto.response.APIResponse;
import com.quy.highconcurrency_ticket_system.dto.response.TicketResponse;
import com.quy.highconcurrency_ticket_system.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<TicketResponse>>> index(){
        APIResponse<List<TicketResponse>> response = new APIResponse<>(HttpStatus.OK.value(), "Tickets retrieved successfully"
                , ticketService.index(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<TicketResponse>> findById(@PathVariable Long id){
        APIResponse<TicketResponse> response = new APIResponse<>(HttpStatus.OK.value(), "Ticket retrieved successfully"
                , ticketService.findById(id), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<APIResponse<TicketResponse>> create(@Valid @RequestBody TicketRequest request){
        APIResponse<TicketResponse> response = new APIResponse<>(HttpStatus.CREATED.value(), "Ticket created successfully"
                , ticketService.create(request), null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<TicketResponse>> update(@PathVariable Long id, @Valid @RequestBody TicketUpdateRq request){
        APIResponse<TicketResponse> response = new APIResponse<>(HttpStatus.OK.value(), "Ticket updated successfully"
                , ticketService.update(id, request), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id){
        ticketService.delete(id);
        APIResponse<Object> response = new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Ticket deleted successfully"
                , null, null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
