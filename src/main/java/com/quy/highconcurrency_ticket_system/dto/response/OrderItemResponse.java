package com.quy.highconcurrency_ticket_system.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponse {
    OrderResponse order;
    UserResponse user;
    private int quantity;
    private BigDecimal priceAtPurchase;
    private String ticketType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String eventName;
}
