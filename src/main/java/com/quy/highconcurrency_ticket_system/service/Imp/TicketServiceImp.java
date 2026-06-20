package com.quy.highconcurrency_ticket_system.service.Imp;

import com.quy.highconcurrency_ticket_system.dto.request.TicketRequest;
import com.quy.highconcurrency_ticket_system.dto.request.TicketUpdateRq;
import com.quy.highconcurrency_ticket_system.dto.response.TicketResponse;
import com.quy.highconcurrency_ticket_system.enums.TicketType;
import com.quy.highconcurrency_ticket_system.exception.ResourceNotFoundException;
import com.quy.highconcurrency_ticket_system.model.EventSession;
import com.quy.highconcurrency_ticket_system.model.Ticket;
import com.quy.highconcurrency_ticket_system.repository.EventSessionRepository;
import com.quy.highconcurrency_ticket_system.repository.TicketRepository;
import com.quy.highconcurrency_ticket_system.service.TicketService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImp implements TicketService {

    private final TicketRepository ticketRepository;
    private final EventSessionRepository sessionRepository;
    private final StringRedisTemplate redisTemplate;

    public TicketServiceImp(TicketRepository ticketRepository, EventSessionRepository sessionRepository, StringRedisTemplate redisTemplate) {
        this.ticketRepository = ticketRepository;
        this.sessionRepository = sessionRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public TicketResponse create(TicketRequest request) {
        EventSession eventSession = sessionRepository.findById(request.getEventSessionId()).orElseThrow(() -> new ResourceNotFoundException("Event Session", "Id", request.getEventSessionId()));
        if(request.getAvailableStock() > request.getTotalStock()){
            throw new IllegalArgumentException("Available stock cannot exceed total stock");
        }
        Ticket ticket = Ticket.builder()
                .eventSession(eventSession)
                .type(TicketType.valueOf(request.getType().toUpperCase()))
                .price(request.getPrice())
                .totalStock(request.getTotalStock())
                .availableStock(request.getAvailableStock())
                .build();
        ticketRepository.save(ticket);
        String redisKey = "ticket:stock:" + ticket.getId();
        redisTemplate.opsForValue().set(redisKey, String.valueOf(ticket.getAvailableStock()));
        return new TicketResponse(ticket);
    }

    @Override
    public List<TicketResponse> index() {
        List<Ticket> tickets = ticketRepository.findAll();
        List<TicketResponse> ticketResponses = new ArrayList<>();
        for(Ticket t: tickets){
            ticketResponses.add(new TicketResponse(t));
        }
        return ticketResponses;
    }

    @Override
    public TicketResponse findById(Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if(ticket.isEmpty()){
            throw new ResourceNotFoundException("Ticket", "Id", id);
        }
        return new TicketResponse(ticket.get());
    }

    @Override
    public TicketResponse update(Long id, TicketUpdateRq request) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if(ticket.isEmpty()){
            throw new ResourceNotFoundException("Ticket", "Id", id);
        }
        Ticket ticketUpdate = ticket.get();
        if(StringUtils.hasText(request.getType())){
            ticketUpdate.setType(TicketType.valueOf(request.getType().toUpperCase()));
        }
        if(StringUtils.hasText(String.valueOf(request.getPrice()))){
            ticketUpdate.setPrice(request.getPrice());
        }
        if(StringUtils.hasText(String.valueOf(request.getTotalStock()))){
            ticketUpdate.setTotalStock(request.getTotalStock());
        }
        if(StringUtils.hasText(String.valueOf(request.getAvailableStock()))){
            ticketUpdate.setAvailableStock(request.getAvailableStock());
        }
        ticketRepository.save(ticketUpdate);
        String redisKey = "ticket:stock:" + ticketUpdate.getId();
        redisTemplate.opsForValue().set(redisKey, String.valueOf(ticketUpdate.getAvailableStock()));
        return new TicketResponse(ticketUpdate);
    }

    @Override
    public void delete(Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if(ticket.isEmpty()){
            throw new ResourceNotFoundException("Ticket", "Id", id);
        }
        Ticket ticketDelete = ticket.get();
        ticketDelete.setDeleted(true);
        ticketRepository.save(ticketDelete);
        String redisKey = "ticket:stock:" + id;
        redisTemplate.delete(redisKey);
    }
}
