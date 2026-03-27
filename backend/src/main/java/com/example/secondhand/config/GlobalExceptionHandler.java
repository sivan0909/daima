package com.example.secondhand.config;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import java.util.Map;

/**
 * 全局异常处理器
 * 修复：捕获权限异常，返回JSON格式403响应
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 修复：参数校验失败时，返回准确错误信息（JSR-380）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String msg = "参数校验失败";
        FieldError fe = e.getBindingResult().getFieldError();
        if (fe != null && fe.getDefaultMessage() != null && !fe.getDefaultMessage().isBlank()) {
            msg = fe.getDefaultMessage();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("code", 400, "msg", msg));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException e) {
        String msg = (e.getMessage() == null || e.getMessage().isBlank()) ? "参数校验失败" : e.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("code", 400, "msg", msg));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleNotReadable(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("code", 400, "msg", "请求体格式错误"));
    }

    /**
     * 处理权限不足异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("code", 403, "msg", "无权限"));
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("code", 500, "msg", "服务器内部错误：" + e.getMessage()));
    }
}
