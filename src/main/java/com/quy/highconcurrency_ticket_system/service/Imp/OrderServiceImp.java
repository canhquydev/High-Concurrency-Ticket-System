package com.quy.highconcurrency_ticket_system.service.Imp;

import com.quy.highconcurrency_ticket_system.dto.request.OrderRequest;
import com.quy.highconcurrency_ticket_system.dto.response.OrderResponse;
import com.quy.highconcurrency_ticket_system.enums.OrderStatus;
import com.quy.highconcurrency_ticket_system.exception.ResourceNotFoundException;
import com.quy.highconcurrency_ticket_system.model.Order;
import com.quy.highconcurrency_ticket_system.repository.OrderRepository;
import com.quy.highconcurrency_ticket_system.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImp implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImp(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderResponse create(OrderRequest request) {
        Order order = Order.builder()
                .totalAmount(request.getTotalAmount())
                .status(OrderStatus.valueOf(request.getStatus().toUpperCase()))
                .build();
        orderRepository.save(order);
        return new OrderResponse(order);
    }

    @Override
    public List<OrderResponse> index() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> responses = new ArrayList<>();
        for(Order o: orders){
            responses.add(new OrderResponse(o));
        }
        return responses;
    }

    @Override
    public OrderResponse findById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if(order.isEmpty()){
            throw new ResourceNotFoundException("Order", "Id", id);
        }
        return new OrderResponse(order.get());
    }
}
