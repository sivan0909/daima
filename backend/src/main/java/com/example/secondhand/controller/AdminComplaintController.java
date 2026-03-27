package com.example.secondhand.controller;

import com.example.secondhand.entity.Complaint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/complaints")
@RequiredArgsConstructor
public class AdminComplaintController {

    @GetMapping
    public ResponseEntity<List<Complaint>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status
    ) {
        // 暂时返回空列表，实际项目中需要实现分页查询功能
        List<Complaint> complaints = Collections.emptyList();
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Complaint> getById(@PathVariable Long id) {
        // 暂时返回未找到，实际项目中需要实现根据ID查询功能
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Complaint> create(@RequestBody Complaint complaint) {
        // 暂时返回空，实际项目中需要实现创建投诉功能
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PatchMapping("/{id}/process")
    public ResponseEntity<Complaint> process(@PathVariable Long id, @RequestBody Map<String, String> body) {
        // 暂时返回未找到，实际项目中需要实现处理投诉功能
        return ResponseEntity.notFound().build();
    }
}
