package com.example.secondhand.controller;

import com.example.secondhand.entity.Product;
import com.example.secondhand.security.TokenUtil;
import com.example.secondhand.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/seller/products")
@RequiredArgsConstructor
public class SellerProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ) {
        Long sellerId = getCurrentSellerId(request);
        Page<Product> products = productService.findPageBySeller(sellerId, page, size, keyword);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(
            HttpServletRequest request,
            @RequestBody Product product
    ) {
        Long sellerId = getCurrentSellerId(request);
        Product saved = productService.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestBody Product product
    ) {
        Long sellerId = getCurrentSellerId(request);
        return productService.update(id, sellerId, product)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        Long sellerId = getCurrentSellerId(request);
        boolean deleted = productService.delete(id, sellerId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Product> updateStatus(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestParam String status
    ) {
        Long sellerId = getCurrentSellerId(request);
        return productService.updateStatus(id, sellerId, status)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private Long getCurrentSellerId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            TokenUtil.ParsedToken parsed = TokenUtil.parseToken(token);
            if (parsed != null && "seller".equals(parsed.role())) {
                return parsed.userId();
            }
        }
        throw new SecurityException("Invalid seller token");
    }
}
