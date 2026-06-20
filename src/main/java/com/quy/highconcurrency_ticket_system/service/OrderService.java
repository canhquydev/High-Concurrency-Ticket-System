package com.quy.highconcurrency_ticket_system.service;

import com.quy.highconcurrency_ticket_system.dto.request.OrderRequest;
import com.quy.highconcurrency_ticket_system.dto.response.OrderItemResponse;
import com.quy.highconcurrency_ticket_system.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    String create(OrderRequest request);
    List<OrderResponse> index();
    OrderResponse findById(Long id);
    OrderItemResponse orderDetails(Long orderId);
}
