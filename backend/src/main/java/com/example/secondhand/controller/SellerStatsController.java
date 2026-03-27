package com.example.secondhand.controller;

import com.example.secondhand.security.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;

/**
 * 卖家统计数据控制器
 * 提供销售额、销量、商品数量等统计信息
 */
@RestController
@RequestMapping("/api/seller/stats")
@RequiredArgsConstructor
public class SellerStatsController {

    /**
     * 获取卖家统计数据
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getStats(HttpServletRequest request) {
        Long sellerId = getCurrentSellerId(request);
        
        // 返回模拟数据，实际项目中需要从数据库查询
        Map<String, Object> stats = new HashMap<>();
        stats.put("salesAmount", new BigDecimal(12345.67));
        stats.put("salesCount", 156);
        stats.put("productCount", 28);
        stats.put("orderCount", 123);
        stats.put("productsOnSale", 20);
        stats.put("productsOffSale", 8);
        stats.put("ordersPending", 5);
        stats.put("unreadEvaluations", 3);
        
        return ResponseEntity.ok(stats);
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
