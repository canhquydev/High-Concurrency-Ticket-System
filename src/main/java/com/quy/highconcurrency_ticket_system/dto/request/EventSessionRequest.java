package com.quy.highconcurrency_ticket_system.dto.request;

import com.quy.highconcurrency_ticket_system.anotation.EnumValid;
import com.quy.highconcurrency_ticket_system.enums.SessionStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventSessionRequest {
    @NotBlank
    private String location;
    @NotNull
    private LocalDateTime startTime;
    @NotNull
    @Future
    private LocalDateTime endTime;
    @NotBlank
    @EnumValid(enumClass = SessionStatus.class, message = "Session status  is not valid")
    private String status;
}
