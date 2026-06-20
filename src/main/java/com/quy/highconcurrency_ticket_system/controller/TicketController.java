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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/admin/tickets")
@Tag(name = "Ticket", description = "Ticket Management APIs")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Operation(summary = "Get all tickets", description = "Retrieve a list of all tickets")
    @ApiResponse(responseCode = "200", description = "Tickets retrieved successfully")
    @GetMapping
    public ResponseEntity<APIResponse<List<TicketResponse>>> index(){
        APIResponse<List<TicketResponse>> response = new APIResponse<>(HttpStatus.OK.value(), "Tickets retrieved successfully"
                , ticketService.index(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get ticket by ID", description = "Retrieve a ticket by its ID")
    @ApiResponse(responseCode = "200", description = "Ticket retrieved successfully")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<TicketResponse>> findById(@PathVariable Long id){
        APIResponse<TicketResponse> response = new APIResponse<>(HttpStatus.OK.value(), "Ticket retrieved successfully"
                , ticketService.findById(id), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Create a ticket", description = "Create a new ticket")
    @ApiResponse(responseCode = "201", description = "Ticket created successfully")
    @PostMapping()
    public ResponseEntity<APIResponse<TicketResponse>> create(@Valid @RequestBody TicketRequest request){
        APIResponse<TicketResponse> response = new APIResponse<>(HttpStatus.CREATED.value(), "Ticket created successfully"
                , ticketService.create(request), null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a ticket", description = "Update an existing ticket by its ID")
    @ApiResponse(responseCode = "200", description = "Ticket updated successfully")
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<TicketResponse>> update(@PathVariable Long id, @Valid @RequestBody TicketUpdateRq request){
        APIResponse<TicketResponse> response = new APIResponse<>(HttpStatus.OK.value(), "Ticket updated successfully"
                , ticketService.update(id, request), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Delete a ticket", description = "Delete a ticket by its ID")
    @ApiResponse(responseCode = "204", description = "Ticket deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id){
        ticketService.delete(id);
        APIResponse<Object> response = new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Ticket deleted successfully"
                , null, null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
