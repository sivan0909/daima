package com.example.secondhand.security;

import com.example.secondhand.entity.User;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

/**
 * JWT Token 工具类
 * 用于生成和解析 JWT Token
 */
public class TokenUtil {

    private static final String SECRET_KEY = "secondhand_platform_secret_key_for_jwt_token_generation";
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000; // 24小时

    public record ParsedToken(Long userId, String username, String role) {
    }

    /**
     * 生成 JWT Token
     * 包含用户ID、用户名、角色信息
     */
    public static String generateToken(User user) {
        if (user == null || user.getId() == null) {
            return null;
        }
        
        long now = System.currentTimeMillis();
        long exp = now + EXPIRE_TIME;
        
        // 构建 JWT Header
        String header = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));
        
        // 构建 JWT Payload
        String payload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(("{\"sub\":\"" + user.getId() + "\"," +
                        "\"username\":\"" + (user.getUsername() != null ? user.getUsername() : "") + "\"," +
                        "\"role\":\"" + (user.getRole() != null ? user.getRole() : "user") + "\"," +
                        "\"iat\":" + now + "," +
                        "\"exp\":" + exp + "}").getBytes(StandardCharsets.UTF_8));
        
        // 构建签名
        String signature = hmacSha256(header + "." + payload, SECRET_KEY);
        
        return header + "." + payload + "." + signature;
    }

    /**
     * 解析 JWT Token
     * 返回 ParsedToken 包含用户ID、用户名、角色
     */
    public static ParsedToken parseToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                // 兼容旧版 demo_token 格式
                if (token.startsWith("demo_token_")) {
                    String userIdStr = token.substring("demo_token_".length());
                    Long userId = Long.parseLong(userIdStr);
                    return new ParsedToken(userId, "demo_user", "admin");
                }
                return null;
            }
            
            // 验证签名
            String signature = hmacSha256(parts[0] + "." + parts[1], SECRET_KEY);
            if (!signature.equals(parts[2])) {
                return null;
            }
            
            // 解析 payload
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            
            // 手动解析 JSON（避免引入额外依赖）
            Long userId = extractLongValue(payload, "sub");
            String username = extractStringValue(payload, "username");
            String role = extractStringValue(payload, "role");
            
            // 检查 Token 是否过期
            Long exp = extractLongValue(payload, "exp");
            if (exp != null && exp < System.currentTimeMillis()) {
                return null; // Token 已过期
            }
            
            return new ParsedToken(userId, username, role);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 验证 token 是否有效
     */
    public static boolean validateToken(String token) {
        return parseToken(token) != null;
    }

    /**
     * HMAC-SHA256 签名
     */
    private static String hmacSha256(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate HMAC-SHA256", e);
        }
    }

    /**
     * 从 JSON 字符串中提取字符串值
     */
    private static String extractStringValue(String json, String key) {
        String pattern = "\"" + key + "\":\"";
        int start = json.indexOf(pattern);
        if (start == -1) return null;
        start += pattern.length();
        int end = json.indexOf("\"", start);
        if (end == -1) return null;
        return json.substring(start, end);
    }

    /**
     * 从 JSON 字符串中提取长整型值
     */
    private static Long extractLongValue(String json, String key) {
        String pattern = "\"" + key + "\":";
        int start = json.indexOf(pattern);
        if (start == -1) return null;
        start += pattern.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        if (end == -1) return null;
        String value = json.substring(start, end).trim();
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
