package com.quy.highconcurrency_ticket_system.repository;

import com.quy.highconcurrency_ticket_system.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
