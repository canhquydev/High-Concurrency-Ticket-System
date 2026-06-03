package com.quy.highconcurrency_ticket_system.dto.request;

import com.quy.highconcurrency_ticket_system.anotation.EnumValid;
import com.quy.highconcurrency_ticket_system.enums.SessionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventSessionUpdateRq {
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @EnumValid(enumClass = SessionStatus.class, message = "Session status is not valid")
    private String status;
}
