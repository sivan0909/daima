package com.example.secondhand.controller;

import com.example.secondhand.entity.User;
import com.example.secondhand.security.TokenUtil;
import com.example.secondhand.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/seller")
@RequiredArgsConstructor
public class SellerController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(HttpServletRequest request) {
        Long sellerId = getCurrentSellerId(request);
        return userService.findById(sellerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(
            HttpServletRequest request,
            @RequestBody User profile
    ) {
        Long sellerId = getCurrentSellerId(request);
        profile.setId(sellerId);
        User updated = userService.update(profile);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            HttpServletRequest request,
            @RequestBody Map<String, String> body
    ) {
        Long sellerId = getCurrentSellerId(request);
        String currentPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");
        userService.changePassword(sellerId, currentPassword, newPassword);
        return ResponseEntity.ok(Map.of("message", "密码修改成功"));
    }

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
