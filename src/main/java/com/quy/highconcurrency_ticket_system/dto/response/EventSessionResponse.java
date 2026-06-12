package com.quy.highconcurrency_ticket_system.dto.response;

import com.quy.highconcurrency_ticket_system.enums.SessionStatus;
import com.quy.highconcurrency_ticket_system.model.EventSession;
import com.quy.highconcurrency_ticket_system.model.Ticket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventSessionResponse implements Serializable {
    private Long id;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SessionStatus status;
    private List<TicketResponse> ticketList;
    public EventSessionResponse(EventSession eventSession){
        this.id = eventSession.getId();
        this.location = eventSession.getLocation();
        this.startTime = eventSession.getStartTime();
        this.endTime = eventSession.getEndTime();
        this.status = eventSession.getStatus();
        List<TicketResponse> ticketResponses = new ArrayList<>();
        if(eventSession.getTicketList() != null){
            for (Ticket ticket: eventSession.getTicketList()){
                ticketResponses.add(new TicketResponse(ticket));
            }
        }
        this.ticketList = ticketResponses;
    }
}
