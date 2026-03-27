package com.example.secondhand.controller;

import com.example.secondhand.entity.User;
import com.example.secondhand.security.TokenUtil;
import com.example.secondhand.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 修复：登录接口
     * POST /api/auth/login
     * 接收 username/password，验证通过后返回 JWT Token
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        
        if (username == null || password == null) {
            return ResponseEntity.status(401).body(Map.of("message", "用户名和密码不能为空"));
        }
        
        Optional<User> userOptional = userService.login(username, password);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = TokenUtil.generateToken(user);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", user);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body(Map.of("message", "用户名或密码错误"));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User saved = userService.create(user);
        return ResponseEntity.ok(saved);
    }

    /**
     * 新增：/api/auth/me 接口，获取当前登录用户信息
     * 从 Authorization Header 解析 Token 获取用户信息
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "未提供有效的认证信息"));
        }
        
        String token = authHeader.substring(7);
        TokenUtil.ParsedToken parsed = TokenUtil.parseToken(token);
        
        if (parsed == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Token 无效或已过期"));
        }
        
        User user = userService.findById(parsed.userId()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "用户不存在"));
        }
        return ResponseEntity.ok(user);
    }
}
