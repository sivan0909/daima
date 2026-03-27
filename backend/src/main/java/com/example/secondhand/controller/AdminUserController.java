package com.example.secondhand.controller;

import com.example.secondhand.entity.User;
import com.example.secondhand.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<User>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status
    ) {
        Page<User> result = userService.findPage(page, size, keyword, role, status);
        return ResponseEntity.ok(result);
    }

    /**
     * 修复：新增用户接口返回标准JSON，避免前端解析失败
     */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user) {
        // 参数校验
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("code", 400, "msg", "用户名不能为空"));
        }
        // 修复：密码校验规则为min:6，移除多余格式限制（留空则默认 123456）
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            user.setPassword("123456");
        }
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("code", 400, "msg", "角色不能为空"));
        }
        
        try {
            User created = userService.create(user);
            return ResponseEntity.ok(Map.of("code", 200, "msg", "新增成功", "data", created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("code", 500, "msg", "新增失败：" + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        return userService.update(id, user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return userService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<User> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body != null ? body.get("status") : null;
        return userService.updateStatus(id, status)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@PathVariable Long id) {
        return userService.resetPassword(id)
                .map(pwd -> ResponseEntity.ok(Map.of("password", pwd)))
                .orElse(ResponseEntity.notFound().build());
    }
}
