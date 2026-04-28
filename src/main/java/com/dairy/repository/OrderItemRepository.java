package com.dairy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dairy.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
