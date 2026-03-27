package com.example.secondhand.controller;

import com.example.secondhand.entity.Evaluation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/evaluation")
@RequiredArgsConstructor
public class EvaluationController {

    @PostMapping("/add")
    public ResponseEntity<Evaluation> add(@RequestBody Object request) {
        // 暂时返回空，实际项目中需要实现添加评价功能
        return ResponseEntity.ok(null);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Evaluation>> listByProduct(@PathVariable Long productId) {
        // 暂时返回空列表，实际项目中需要实现获取商品评价功能
        List<Evaluation> evaluations = Collections.emptyList();
        return ResponseEntity.ok(evaluations);
    }
}

