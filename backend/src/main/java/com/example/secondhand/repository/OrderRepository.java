package com.example.secondhand.repository;

import com.example.secondhand.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByBuyerIdOrderByCreateDateDesc(Long buyerId);
}
