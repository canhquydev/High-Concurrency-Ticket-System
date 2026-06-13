package com.quy.highconcurrency_ticket_system.dto.request;

import com.quy.highconcurrency_ticket_system.anotation.EnumValid;
import com.quy.highconcurrency_ticket_system.enums.TicketType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequest {
    @NotEmpty
    private Long eventSessionId;
    @NotBlank
    @EnumValid(enumClass = TicketType.class, message = "Ticket type is not valid")
    private String type;
    @NotNull
    @DecimalMin(value = "0.1")
    private BigDecimal price;
    @NotNull
    @Min(value = 1)
    private Integer totalStock;
    @NotNull
    @Min(value = 1)
    private Integer availableStock;
}
