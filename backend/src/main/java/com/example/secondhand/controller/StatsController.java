package com.example.secondhand.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    /**
     * 获取商品统计数据
     */
    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getProductStats() {
        return ResponseEntity.ok(Map.of(
                "total", 0,
                "active", 0,
                "inactive", 0,
                "categories", 0
        ));
    }

    /**
     * 获取订单统计数据
     */
    @GetMapping("/orders")
    public ResponseEntity<Map<String, Object>> getOrderStats() {
        return ResponseEntity.ok(Map.of(
                "total", 0,
                "pending", 0,
                "completed", 0,
                "revenue", 0
        ));
    }

    /**
     * 获取用户统计数据
     */
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getUserStats() {
        return ResponseEntity.ok(Map.of(
                "total", 0,
                "buyers", 0,
                "sellers", 0,
                "admins", 0
        ));
    }

    /**
     * 获取概览统计数据
     */
    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getOverviewStats() {
        return ResponseEntity.ok(Map.of(
                "products", 0,
                "orders", 0,
                "users", 0,
                "revenue", 0
        ));
    }

    /**
     * 获取月度统计数据
     */
    @GetMapping("/monthly")
    public ResponseEntity<Object> getMonthlyStats() {
        return ResponseEntity.ok(Map.of());
    }

    /**
     * 获取分类统计数据
     */
    @GetMapping("/categories")
    public ResponseEntity<Object> getCategoryStats() {
        return ResponseEntity.ok(Map.of());
    }
}
