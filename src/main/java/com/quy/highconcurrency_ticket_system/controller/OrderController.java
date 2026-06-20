package com.quy.highconcurrency_ticket_system.controller;

import com.quy.highconcurrency_ticket_system.dto.request.OrderRequest;
import com.quy.highconcurrency_ticket_system.dto.response.APIResponse;
import com.quy.highconcurrency_ticket_system.dto.response.OrderItemResponse;
import com.quy.highconcurrency_ticket_system.dto.response.OrderResponse;
import com.quy.highconcurrency_ticket_system.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
@Tag(name = "Order", description = "Order Management APIs")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Get all orders", description = "Retrieve a list of all orders")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    @GetMapping
    public ResponseEntity<APIResponse<List<OrderResponse>>> index(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        log.info("ROLE: {}", auth.getAuthorities());
        APIResponse<List<OrderResponse>> response = new APIResponse<>(HttpStatus.OK.value(), "Orders retrieved successfully"
                , orderService.index(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get order by ID", description = "Retrieve an order by its ID")
    @ApiResponse(responseCode = "200", description = "Order retrieved successfully")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<OrderResponse>> findById(@PathVariable Long id){
        APIResponse<OrderResponse> response = new APIResponse<>(HttpStatus.OK.value(), "Order retrieved successfully"
                , orderService.findById(id), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Operation(summary = "Get order details", description = "Retrieve details of a specific order by ID")
    @ApiResponse(responseCode = "200", description = "Order details retrieved successfully")
    @GetMapping("/details/{id}")
    public ResponseEntity<APIResponse<OrderItemResponse>> find(@PathVariable Long id){
        APIResponse<OrderItemResponse> response = new APIResponse<>(HttpStatus.OK.value(), "Order retrieved successfully"
                , orderService.orderDetails(id), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "Create an order", description = "Create a new order")
    @ApiResponse(responseCode = "201", description = "Order created successfully")
    @PostMapping
    public ResponseEntity<APIResponse<String>> create(@Valid @RequestBody OrderRequest request){
        APIResponse<String> response = new APIResponse<>(HttpStatus.CREATED.value(), "Order created successfully"
                , orderService.create(request), null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
