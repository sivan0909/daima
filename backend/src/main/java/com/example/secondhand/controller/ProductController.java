package com.example.secondhand.controller;

import com.example.secondhand.entity.Product;
import com.example.secondhand.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private static final String STATUS_ON_SALE = "上架";

    private final ProductRepository productRepository;

    /**
     * 前台商品列表 / 搜索
     * GET /api/products?keyword=xxx
     * 前端期望：返回数组或 { data: [...] }，这里直接返回数组即可
     */
    @GetMapping
    public ResponseEntity<List<Product>> list(@RequestParam(required = false) String keyword) {
        List<Product> result;
        if (keyword != null && !keyword.trim().isEmpty()) {
            result = productRepository.findByStatusAndNameContainingOrderByCreatedAtDesc(
                    STATUS_ON_SALE,
                    keyword.trim()
            );
        } else {
            result = productRepository.findByStatusOrderByCreatedAtDesc(STATUS_ON_SALE);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 精品推荐：返回最新的若干条上架商品（默认 4 条）
     * GET /api/products/recommended
     */
    @GetMapping("/recommended")
    public ResponseEntity<List<Product>> recommended() {
        List<Product> list = productRepository.findTop4ByStatusOrderByCreatedAtDesc(STATUS_ON_SALE);
        return ResponseEntity.ok(list);
    }
}

