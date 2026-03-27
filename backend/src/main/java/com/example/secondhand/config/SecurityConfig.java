package com.example.secondhand.config;

import com.example.secondhand.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 修复：Spring Security 配置 admin 角色权限
     * 放行登录/注册接口，其他接口需要认证
     * /api/admin/** 仅允许 admin 角色访问
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用 CSRF（前后端分离项目）
            .csrf(csrf -> csrf.disable())
            // 配置 CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // 无状态会话（使用 JWT）
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 添加 JWT 过滤器
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            // 配置请求权限
            .authorizeHttpRequests(auth -> auth
                // 公开接口：登录、注册、静态资源
                .requestMatchers("/api/auth/login", "/api/auth/register", 
                                "/api/user/register", "/h2-console/**").permitAll()
                // 管理员接口：仅允许 admin 角色
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // 卖家接口：允许 seller 或 admin 角色
                .requestMatchers("/api/seller/**").hasAnyRole("SELLER", "ADMIN")
                // 其他接口需要认证
                .anyRequest().authenticated()
            )
            // 禁用表单登录（使用 JWT）
            .formLogin(form -> form.disable())
            // 禁用 HTTP Basic
            .httpBasic(basic -> basic.disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
