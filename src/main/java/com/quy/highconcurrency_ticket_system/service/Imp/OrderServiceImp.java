package com.quy.highconcurrency_ticket_system.service.Imp;

import com.quy.highconcurrency_ticket_system.dto.request.OrderRequest;
import com.quy.highconcurrency_ticket_system.dto.response.OrderItemResponse;
import com.quy.highconcurrency_ticket_system.dto.response.OrderResponse;
import com.quy.highconcurrency_ticket_system.dto.response.UserResponse;
import com.quy.highconcurrency_ticket_system.enums.OrderStatus;
import com.quy.highconcurrency_ticket_system.exception.InsufficientTicketsException;
import com.quy.highconcurrency_ticket_system.exception.ResourceNotFoundException;
import com.quy.highconcurrency_ticket_system.model.*;
import com.quy.highconcurrency_ticket_system.repository.*;
import com.quy.highconcurrency_ticket_system.service.OrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImp implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderServiceImp(OrderRepository orderRepository, UserRepository userRepository, TicketRepository ticketRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderItemResponse create(OrderRequest request){

        var authenticated = SecurityContextHolder.getContext().getAuthentication();
        assert authenticated != null;
        String email = authenticated.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("User", "Email", email));
        Ticket ticket = ticketRepository.findById(request.getTicketId()).orElseThrow(() ->
                new ResourceNotFoundException("Ticket", "Ticket Id", request.getTicketId()));
        BigDecimal totalAmount = ticket.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));
        if(request.getQuantity() > ticket.getAvailableStock()){
            throw new InsufficientTicketsException("Insufficient ticket quantity available.");
        }
        Order order = Order.builder()
                .user(user)
                .totalAmount(totalAmount)
                .status(OrderStatus.PENDING)
                .build();
        orderRepository.save(order);

        ticket.setAvailableStock(ticket.getAvailableStock() - request.getQuantity());
        ticketRepository.save(ticket);

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .ticket(ticket)
                .quantity(request.getQuantity())
                .price(ticket.getPrice())
                .build();
        orderItemRepository.save(orderItem);
        return OrderItemResponse.builder()
                .order(new OrderResponse(order))
                .user(new UserResponse(user))
                .quantity(request.getQuantity())
                .priceAtPurchase(ticket.getPrice())
                .ticketType(ticket.getType().toString())
                .startTime(ticket.getEventSession().getStartTime())
                .endTime(ticket.getEventSession().getEndTime())
                .eventName(ticket.getEventSession().getEvent().getName())
                .build();
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

    @Override
    public OrderItemResponse orderDetails(Long orderId) {
        Optional<OrderItem> order = orderItemRepository.findByIdWithDetails(orderId);
        if(order.isEmpty()){
            throw new ResourceNotFoundException("Order Item", "Id", orderId);
        }
        OrderItem orderItem = order.get();
        return OrderItemResponse.builder()
                .order(new OrderResponse(orderItem.getOrder()))
                .user(new UserResponse(orderItem.getOrder().getUser()))
                .quantity(orderItem.getQuantity())
                .priceAtPurchase(orderItem.getPrice())
                .ticketType(orderItem.getTicket().getType().toString())
                .startTime(orderItem.getTicket().getEventSession().getStartTime())
                .endTime(orderItem.getTicket().getEventSession().getEndTime())
                .eventName(orderItem.getTicket().getEventSession().getEvent().getName())
                .build();
    }
}
