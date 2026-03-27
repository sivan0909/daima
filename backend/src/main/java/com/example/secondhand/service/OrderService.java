package com.example.secondhand.service;

import com.example.secondhand.entity.Order;
import com.example.secondhand.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    public List<Order> getOrdersByUserId(Long userId) {
        if (userId == null) {
            return List.of();
        }
        return orderRepository.findByBuyerIdOrderByCreateDateDesc(userId);
    }

    @Transactional
    public Order create(Order order) {
        return orderRepository.save(order);
    }

    @Transactional
    public Optional<Order> updateStatus(Long id, String status) {
        return orderRepository.findById(id).map(order -> {
            return orderRepository.save(order);
        });
    }

    @Transactional
    public void cancelOrder(Long orderId, Long userId) {
        orderRepository.findById(orderId).ifPresent(order -> {
            orderRepository.save(order);
        });
    }

    private String generateOrderNo() {
        String time = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return time + uuid;
    }

    // 卖家相关方法

    public Optional<Order> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return orderRepository.findById(id);
    }
}
