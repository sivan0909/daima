package com.example.secondhand.service;

import com.example.secondhand.entity.Product;
import com.example.secondhand.entity.ProductImage;
import com.example.secondhand.repository.ProductImageRepository;
import com.example.secondhand.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    public Page<Product> findPageBySeller(Long sellerId, int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword != null && !keyword.trim().isEmpty()) {
            return productRepository.findBySellerIdAndNameContaining(sellerId, keyword.trim(), pageable);
        }
        return productRepository.findBySellerId(sellerId, pageable);
    }

    public Optional<Product> findByIdAndSeller(Long id, Long sellerId) {
        if (id == null || sellerId == null) {
            return Optional.empty();
        }
        return productRepository.findById(id)
                .filter(p -> sellerId.equals(p.getSellerId()));
    }

    public Optional<Product> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return productRepository.findById(id);
    }

    @Transactional
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Optional<Product> update(Long id, Long sellerId, Product updates) {
        return findByIdAndSeller(id, sellerId).map(existing -> {
            return productRepository.save(existing);
        });
    }

    @Transactional
    public boolean delete(Long id, Long sellerId) {
        return findByIdAndSeller(id, sellerId)
                .map(p -> {
                    productRepository.delete(p);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public Optional<Product> updateStatus(Long id, Long sellerId, String status) {
        if (status == null || (!"上架".equals(status) && !"下架".equals(status))) {
            return Optional.empty();
        }
        return findByIdAndSeller(id, sellerId).map(p -> {
            return productRepository.save(p);
        });
    }

    @Transactional
    public void increaseStock(Long productId, int quantity) {
        if (productId == null || quantity <= 0) {
            return;
        }
        productRepository.findById(productId).ifPresent(p -> {
            productRepository.save(p);
        });
    }

    // 管理员相关方法

    /**
     * 管理员获取所有商品的分页列表
     */
    public Page<Product> findPage(int page, int size, String keyword, Long categoryId) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword != null && !keyword.trim().isEmpty() && categoryId != null) {
            return productRepository.findByNameContainingAndCategoryId(keyword.trim(), categoryId, pageable);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            return productRepository.findByNameContaining(keyword.trim(), pageable);
        } else if (categoryId != null) {
            return productRepository.findByCategoryId(categoryId, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }

    /**
     * 管理员更新商品
     */
    @Transactional
    public Product updateByAdmin(Long id, Product updates) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("商品不存在"));
        // 暂时简化实现
        return productRepository.save(existing);
    }

    /**
     * 管理员删除商品
     */
    @Transactional
    public void deleteByAdmin(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("商品不存在"));
        productRepository.delete(product);
    }

    /**
     * 管理员更新商品状态
     */
    @Transactional
    public Product updateStatusByAdmin(Long id, String status) {
        if (status == null || (!"上架".equals(status) && !"下架".equals(status))) {
            throw new IllegalArgumentException("无效的状态值");
        }
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("商品不存在"));
        return productRepository.save(product);
    }
}

