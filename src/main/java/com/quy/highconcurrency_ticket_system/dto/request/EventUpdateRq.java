package com.quy.highconcurrency_ticket_system.dto.request;
import com.quy.highconcurrency_ticket_system.anotation.EnumValid;
import com.quy.highconcurrency_ticket_system.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventUpdateRq {
    private String name;
    private String description;
    @EnumValid(enumClass = EventStatus.class, message = "Event is not valid")
    private String status;
}
