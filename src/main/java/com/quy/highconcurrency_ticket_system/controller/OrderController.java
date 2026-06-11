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

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<OrderResponse>>> index(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        log.info("ROLE: {}", auth.getAuthorities());
        APIResponse<List<OrderResponse>> response = new APIResponse<>(HttpStatus.OK.value(), "Orders retrieved successfully"
                , orderService.index(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<OrderResponse>> findById(@PathVariable Long id){
        APIResponse<OrderResponse> response = new APIResponse<>(HttpStatus.OK.value(), "Order retrieved successfully"
                , orderService.findById(id), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/details/{id}")
    public ResponseEntity<APIResponse<OrderItemResponse>> find(@PathVariable Long id){
        APIResponse<OrderItemResponse> response = new APIResponse<>(HttpStatus.OK.value(), "Order retrieved successfully"
                , orderService.orderDetails(id), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<APIResponse<OrderItemResponse>> create(@Valid @RequestBody OrderRequest request){
        APIResponse<OrderItemResponse> response = new APIResponse<>(HttpStatus.CREATED.value(), "Order created successfully"
                , orderService.create(request), null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
