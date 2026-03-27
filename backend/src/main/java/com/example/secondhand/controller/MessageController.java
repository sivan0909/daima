package com.example.secondhand.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    /**
     * 获取消息列表
     */
    @GetMapping
    public ResponseEntity<List<Object>> getMessages() {
        return ResponseEntity.ok(List.of());
    }
}
