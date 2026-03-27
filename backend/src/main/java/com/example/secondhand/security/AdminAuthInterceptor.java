package com.example.secondhand.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 简单的管理员鉴权拦截器：
 * 拦截 /api/admin/** 请求，要求
 * - 存在 Bearer Token
 * - Token 有效且未过期
 * - 角色为 admin
 */
@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 放行OPTIONS预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "未提供有效的管理员令牌");
            return false;
        }
        String token = authHeader.substring("Bearer ".length()).trim();
        TokenUtil.ParsedToken parsed = TokenUtil.parseToken(token);
        if (parsed == null) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "令牌无效或已过期");
            return false;
        }
        if (!"admin".equalsIgnoreCase(parsed.role())) {
            writeError(response, HttpServletResponse.SC_FORBIDDEN, "无权访问管理员接口");
            return false;
        }
        // 通过后，可将解析结果放入请求属性，供控制器按需使用
        request.setAttribute("authUser", parsed);
        return true;
    }

    private void writeError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json;charset=UTF-8");
        String body = "{\"message\":\"" + message + "\"}";
        response.getWriter().write(body);
    }
}

