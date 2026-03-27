package com.example.secondhand.controller;

import com.example.secondhand.entity.AfterSale;
import com.example.secondhand.service.AfterSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/after-sale")
@RequiredArgsConstructor
public class AfterSaleController {

    private final AfterSaleService afterSaleService;

    @PostMapping("/apply")
    public ResponseEntity<AfterSale> apply(
            @RequestParam Long userId,
            @RequestParam Long orderId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String reason
    ) {
        return ResponseEntity.ok(afterSaleService.apply(userId, orderId, type, reason));
    }

    /**
     * 买家点击“平台介入”
     */
    @PostMapping("/{id}/platform-intervene")
    public ResponseEntity<AfterSale> platformIntervene(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(afterSaleService.requestPlatformIntervene(id, userId));
    }
}

