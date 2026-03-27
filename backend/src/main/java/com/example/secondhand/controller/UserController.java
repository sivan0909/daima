package com.example.secondhand.controller;

import com.example.secondhand.entity.User;
import com.example.secondhand.security.TokenUtil;
import com.example.secondhand.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 注册接口：前端调用 /api/user/register
     * 请求体示例：{ "username": "...", "password": "...", "role": "buyer" }
     * 密码会在 UserService 中使用 BCrypt 加密后保存
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody User user) {
        // 修复：密码校验规则为min:6，移除多余格式限制（留空则默认 123456）
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            user.setPassword("123456");
        }
        User saved = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("code", 201, "msg", "注册成功", "data", saved));
    }

    /**
     * 修复：/api/user/profile 接口参数解析逻辑
     * 从 Authorization header 解析 JWT token 获取用户信息
     * 支持两种传参方式：Header 中的 Token 或 URL 参数 userId
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "userId", required = false) Long userIdParam
    ) {
        Long userId = null;
        
        // 优先从 Token 解析 userId
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring("Bearer ".length()).trim();
            TokenUtil.ParsedToken parsed = TokenUtil.parseToken(token);
            if (parsed != null) {
                userId = parsed.userId();
            }
        }
        
        // 如果 Token 解析失败，尝试从 URL 参数获取
        if (userId == null && userIdParam != null) {
            userId = userIdParam;
        }
        
        // 如果都没有获取到 userId，返回 401
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "未提供有效的认证信息或用户ID"));
        }
        
        User user = userService.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "用户不存在"));
        }
        return ResponseEntity.ok(user);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody User userUpdates
    ) {
        Long userId = null;
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring("Bearer ".length()).trim();
            TokenUtil.ParsedToken parsed = TokenUtil.parseToken(token);
            if (parsed != null) {
                userId = parsed.userId();
            }
        }
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "未提供有效的认证信息"));
        }
        
        User updated = userService.update(userId, userUpdates).orElse(null);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "用户不存在"));
        }
        return ResponseEntity.ok(updated);
    }
}
