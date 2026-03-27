package com.example.secondhand.service;

import com.example.secondhand.entity.AfterSale;
import com.example.secondhand.repository.AfterSaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AfterSaleService {

    private final AfterSaleRepository afterSaleRepository;

    @Transactional
    public AfterSale apply(Long userId, Long orderId, String type, String reason) {
        if (userId == null || orderId == null) {
            throw new IllegalArgumentException("参数不完整");
        }
        AfterSale as = AfterSale.builder()
                .orderId(orderId)
                .userId(userId)
                .type(type != null ? type : "退款")
                .status("待处理")
                .reason(reason)
                .build();
        return afterSaleRepository.save(as);
    }

    @Transactional
    public AfterSale requestPlatformIntervene(Long afterSaleId, Long userId) {
        if (afterSaleId == null || userId == null) {
            throw new IllegalArgumentException("参数不完整");
        }
        return afterSaleRepository.findById(afterSaleId).map(as -> {
            as.setStatus("ARBITRATION");
            return afterSaleRepository.save(as);
        }).orElseThrow(() -> new IllegalArgumentException("售后单不存在"));
    }

    @Transactional
    public AfterSale arbitrate(Long id, Long adminId, String adminRemark, String resultText) {
        if (id == null || adminId == null) {
            throw new IllegalArgumentException("参数不完整");
        }
        return afterSaleRepository.findById(id).map(as -> {
            as.setStatus("RESOLVED");
            return afterSaleRepository.save(as);
        }).orElseThrow(() -> new IllegalArgumentException("售后单不存在"));
    }

    @Transactional(readOnly = true)
    public Page<AfterSale> listByStatus(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (status == null || status.isBlank() || "全部".equals(status)) {
            return afterSaleRepository.findAll(pageable);
        }
        return afterSaleRepository.findByStatus(status, pageable);
    }
}
