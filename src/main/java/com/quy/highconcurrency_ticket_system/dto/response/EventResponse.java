package com.quy.highconcurrency_ticket_system.dto.response;

import com.quy.highconcurrency_ticket_system.enums.EventStatus;
import com.quy.highconcurrency_ticket_system.model.Event;
import com.quy.highconcurrency_ticket_system.model.EventSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventResponse {
    private Long id;
    private String name;
    private String description;
    private String location;
    private LocalDateTime createdAt;
    private EventStatus status;
    private boolean deleted;
    private List<EventSessionResponse> eventSessions;
    public EventResponse(Event event){
        this.id = event.getId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.location = event.getLocation();
        this.createdAt = event.getCreatedAt();
        this.status = event.getStatus();
        this.deleted = event.isDeleted();
        List<EventSessionResponse> eventSessionResponses = new ArrayList<>();
        if(event.getEventSessionsList() != null){
            for (EventSession e : event.getEventSessionsList()) {
                eventSessionResponses.add(new EventSessionResponse(e));
            }
        }
        this.eventSessions = eventSessionResponses;
    }
}
