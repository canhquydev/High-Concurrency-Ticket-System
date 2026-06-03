package com.quy.highconcurrency_ticket_system.model;

import com.quy.highconcurrency_ticket_system.enums.TicketType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private EventSession eventSession;
    @Enumerated(EnumType.STRING)
    private TicketType type;
    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    private Integer totalStock;
    private Integer availableStock;
    private boolean deleted = false;
}
