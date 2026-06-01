package com.quy.highconcurrency_ticket_system.model;

import com.quy.highconcurrency_ticket_system.enums.EventStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private EventStatus status;
    private boolean deleted = false;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EventSession> eventSessionsList;

}
