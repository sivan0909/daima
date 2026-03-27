package com.example.secondhand.controller;

import com.example.secondhand.entity.Order;
import com.example.secondhand.security.TokenUtil;
import com.example.secondhand.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * 卖家订单管理控制器
 * 处理卖家的订单查询、发货等操作
 */
@RestController
@RequestMapping("/api/seller/orders")
@RequiredArgsConstructor
public class SellerOrderController {

    private final OrderService orderService;

    /**
     * 获取卖家订单列表
     */
    @GetMapping
    public ResponseEntity<List<Order>> getOrders(
            HttpServletRequest request
    ) {
        Long sellerId = getCurrentSellerId(request);
        // 暂时返回空列表，实际项目中需要实现按卖家ID查询
        List<Order> orders = Collections.emptyList();
        return ResponseEntity.ok(orders);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderDetail(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        Long sellerId = getCurrentSellerId(request);
        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 发货
     */
    @PatchMapping("/{id}/ship")
    public ResponseEntity<Order> shipOrder(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        Long sellerId = getCurrentSellerId(request);
        // 暂时返回未找到，实际项目中需要实现发货功能
        return ResponseEntity.notFound().build();
    }

    /**
     * 从请求中获取当前卖家ID
     */
    private Long getCurrentSellerId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            TokenUtil.ParsedToken parsed = TokenUtil.parseToken(token);
            if (parsed != null && "seller".equals(parsed.role())) {
                return parsed.userId();
            }
        }
        throw new SecurityException("Invalid seller token");
    }
}
