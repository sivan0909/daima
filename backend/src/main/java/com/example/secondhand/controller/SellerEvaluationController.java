package com.example.secondhand.controller;

import com.example.secondhand.entity.Evaluation;
import com.example.secondhand.security.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * 卖家评价管理控制器
 * 处理卖家的评价查询等操作
 */
@RestController
@RequestMapping("/api/seller/evaluations")
@RequiredArgsConstructor
public class SellerEvaluationController {

    /**
     * 获取卖家评价列表
     */
    @GetMapping
    public ResponseEntity<List<Evaluation>> getEvaluations(
            HttpServletRequest request
    ) {
        Long sellerId = getCurrentSellerId(request);
        // 暂时返回空列表，实际项目中需要实现按卖家ID查询
        List<Evaluation> evaluations = Collections.emptyList();
        return ResponseEntity.ok(evaluations);
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
