package com.example.secondhand.controller;

import com.example.secondhand.entity.Notice;
import com.example.secondhand.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/notices")
@RequiredArgsConstructor
public class AdminNoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<Page<Notice>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ) {
        Page<Notice> result = noticeService.findPage(page, size, keyword);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Notice> create(@RequestBody Notice notice) {
        Notice saved = noticeService.create(notice);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notice> update(@PathVariable Long id, @RequestBody Notice notice) {
        return noticeService.update(id, notice)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return noticeService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/top")
    public ResponseEntity<Notice> updateTop(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        Boolean isTop = body != null && body.containsKey("isTop") ? body.get("isTop") : null;
        if (isTop == null) {
            return ResponseEntity.badRequest().build();
        }
        return noticeService.updateTop(id, isTop)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Notice> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body != null ? body.get("status") : null;
        return noticeService.updateStatus(id, status)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
