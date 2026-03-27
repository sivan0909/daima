package com.example.secondhand.controller;

import com.example.secondhand.entity.Complaint;
import com.example.secondhand.security.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * 卖家投诉管理控制器
 * 处理卖家的投诉查询和处理操作
 */
@RestController
@RequestMapping("/api/seller/complaints")
@RequiredArgsConstructor
public class SellerComplaintController {

    /**
     * 获取卖家投诉列表
     */
    @GetMapping
    public ResponseEntity<List<Complaint>> getComplaints(
            HttpServletRequest request
    ) {
        Long sellerId = getCurrentSellerId(request);
        // 暂时返回空列表，实际项目中需要实现按卖家ID查询
        List<Complaint> complaints = Collections.emptyList();
        return ResponseEntity.ok(complaints);
    }

    /**
     * 处理投诉
     */
    @PatchMapping("/{id}/handle")
    public ResponseEntity<Complaint> handleComplaint(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam String reply
    ) {
        Long sellerId = getCurrentSellerId(request);
        // 暂时返回未找到，实际项目中需要实现处理投诉功能
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
