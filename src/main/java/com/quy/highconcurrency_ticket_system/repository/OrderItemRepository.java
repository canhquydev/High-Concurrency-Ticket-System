package com.quy.highconcurrency_ticket_system.repository;

import com.quy.highconcurrency_ticket_system.model.OrderItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @EntityGraph(attributePaths = {"ticket", "ticket.eventSession", "ticket.eventSession.event", "order", "order.user"})
    @Query("""
    select oi
    from OrderItem oi
    where oi.id = ?1
""")
    Optional<OrderItem> findByIdWithDetails(Long orderItemId);
}
