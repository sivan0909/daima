package com.example.secondhand.controller;

import com.example.secondhand.entity.AfterSale;
import com.example.secondhand.service.AfterSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/after-sale")
@RequiredArgsConstructor
public class AdminAfterSaleController {

    private final AfterSaleService afterSaleService;

    @GetMapping
    public ResponseEntity<Page<AfterSale>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status
    ) {
        return ResponseEntity.ok(afterSaleService.listByStatus(status, page, size));
    }

    /**
     * 管理员仲裁接口：/api/admin/after-sale/arbitrate
     */
    @PostMapping("/arbitrate")
    public ResponseEntity<AfterSale> arbitrate(@RequestBody Map<String, String> body) {
        Long id = body.containsKey("id") ? Long.valueOf(body.get("id")) : null;
        Long adminId = body.containsKey("adminId") ? Long.valueOf(body.get("adminId")) : null;
        String adminRemark = body.getOrDefault("adminRemark", "");
        String resultText = body.getOrDefault("result", "");
        return ResponseEntity.ok(afterSaleService.arbitrate(id, adminId, adminRemark, resultText));
    }
}

