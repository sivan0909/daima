package com.example.secondhand.controller;

import com.example.secondhand.entity.Order;
import com.example.secondhand.security.TokenUtil;
import com.example.secondhand.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(
            HttpServletRequest request,
            @RequestBody Order order
    ) {
        Long buyerId = getCurrentBuyerId(request);
        Order saved = orderService.create(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/buyer")
    public ResponseEntity<List<Order>> getBuyerOrders(HttpServletRequest request) {
        Long buyerId = getCurrentBuyerId(request);
        List<Order> orders = orderService.getOrdersByUserId(buyerId);
        return ResponseEntity.ok(orders);
    }

    /**
     * 新增：GET /api/orders/user
     * 根据 userId 查询该用户的订单列表（买家或卖家）
     */
    @GetMapping("/user")
    public ResponseEntity<List<Order>> getOrdersByUser(
            @RequestParam Long userId,
            HttpServletRequest request
    ) {
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        Long buyerId = getCurrentBuyerId(request);
        orderService.cancelOrder(id, buyerId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestParam String status
    ) {
        return orderService.updateStatus(id, status)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private Long getCurrentBuyerId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            TokenUtil.ParsedToken parsed = TokenUtil.parseToken(token);
            if (parsed != null) {
                return parsed.userId();
            }
        }
        throw new SecurityException("Invalid token");
    }
}
