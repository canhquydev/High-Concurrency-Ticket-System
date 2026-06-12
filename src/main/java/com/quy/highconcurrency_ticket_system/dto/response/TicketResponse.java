package com.quy.highconcurrency_ticket_system.dto.response;

import com.quy.highconcurrency_ticket_system.enums.TicketType;
import com.quy.highconcurrency_ticket_system.model.Ticket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse implements Serializable {
    private TicketType type;
    private BigDecimal price;
    private Integer totalStock;
    private Integer availableStock;
    private boolean deleted;
    public TicketResponse(Ticket ticket){
        this.type = ticket.getType();
        this.price = ticket.getPrice();
        this.totalStock = ticket.getTotalStock();
        this.availableStock = ticket.getAvailableStock();
        this.deleted = ticket.isDeleted();
    }
}
