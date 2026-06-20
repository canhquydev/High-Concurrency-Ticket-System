package com.quy.highconcurrency_ticket_system.service.Imp;

import com.quy.highconcurrency_ticket_system.configuration.RabbitMQConfig;
import com.quy.highconcurrency_ticket_system.dto.request.OrderMessage;
import com.quy.highconcurrency_ticket_system.enums.OrderStatus;
import com.quy.highconcurrency_ticket_system.exception.ResourceNotFoundException;
import com.quy.highconcurrency_ticket_system.model.Order;
import com.quy.highconcurrency_ticket_system.model.Ticket;
import com.quy.highconcurrency_ticket_system.repository.OrderRepository;
import com.quy.highconcurrency_ticket_system.repository.TicketRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderMessageListener {

    private final TicketRepository ticketRepository;
    private final OrderRepository orderRepository;
    private final StringRedisTemplate redisTemplate;

    public OrderMessageListener(TicketRepository ticketRepository, OrderRepository orderRepository, StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.ticketRepository = ticketRepository;
        this.orderRepository = orderRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    @Transactional(rollbackFor = Exception.class)
    public void processOrder(OrderMessage message){
        try {
            Ticket ticket = ticketRepository.findById(message.getTicketId()).orElseThrow(() ->
                    new ResourceNotFoundException("Ticket", "Ticket id", message.getTicketId()));

            ticket.setAvailableStock(ticket.getAvailableStock() - message.getQuantity());

            Order order = orderRepository.findById(message.getOrderId()).orElseThrow(()->
                    new ResourceNotFoundException("Order", "Order id", message.getOrderId()));
            order.setStatus(OrderStatus.PAID);
            System.out.println("Order ID " + order.getId() + " processed successfully.");
        } catch (Exception e){

            String redisKey = "ticket:stock:" + message.getTicketId();
            redisTemplate.opsForValue().increment(redisKey, message.getQuantity());
            Order order = orderRepository.findById(message.getOrderId()).orElseThrow(()->
                    new ResourceNotFoundException("Order", "Order id", message.getOrderId()));
            order.setStatus(OrderStatus.FAILED);

            System.out.println(e.getMessage());
        }

    }
}
