package com.quy.highconcurrency_ticket_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderMessage {
    private Long orderId;
    private Long ticketId;
    private int quantity;
}
