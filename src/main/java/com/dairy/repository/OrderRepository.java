package com.dairy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dairy.model.Order;
import com.dairy.model.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    List<Order> findAllByOrderByCreatedAtDesc();
}
