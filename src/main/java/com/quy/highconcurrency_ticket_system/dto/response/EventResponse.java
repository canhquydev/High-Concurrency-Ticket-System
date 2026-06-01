package com.quy.highconcurrency_ticket_system.dto.response;

import com.quy.highconcurrency_ticket_system.enums.EventStatus;
import com.quy.highconcurrency_ticket_system.model.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private EventStatus status;
    private boolean deleted;
    public EventResponse(Event event){
        this.id = event.getId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.createdAt = event.getCreatedAt();
        this.status = event.getStatus();
        this.deleted = event.isDeleted();
    }
}
