package com.quy.highconcurrency_ticket_system.dto.request;

import com.quy.highconcurrency_ticket_system.anotation.EnumValid;
import com.quy.highconcurrency_ticket_system.enums.EventStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    @NotBlank
    private String name;
    private String description;
    @NotBlank
    private String location;
    @NotBlank
    @EnumValid(enumClass = EventStatus.class, message = "Event is not valid")
    private String status;
}
