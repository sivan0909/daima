package com.example.secondhand.repository;

import com.example.secondhand.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findBySellerId(Long sellerId, Pageable pageable);

    Page<Product> findBySellerIdAndNameContaining(Long sellerId, String keyword, Pageable pageable);

    /**
     * 前台商品列表：仅展示上架商品，按创建时间倒序
     */
    List<Product> findByStatusOrderByCreatedAtDesc(String status);

    /**
     * 前台搜索：根据名称模糊匹配上架商品
     */
    List<Product> findByStatusAndNameContainingOrderByCreatedAtDesc(String status, String keyword);

    /**
     * 精品推荐：取最新的前若干条上架商品
     */
    List<Product> findTop4ByStatusOrderByCreatedAtDesc(String status);

    /**
     * 管理员搜索：根据名称模糊匹配所有商品
     */
    Page<Product> findByNameContaining(String keyword, Pageable pageable);

    /**
     * 管理员搜索：根据分类获取所有商品
     */
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * 管理员搜索：根据名称和分类联合搜索所有商品
     */
    Page<Product> findByNameContainingAndCategoryId(String keyword, Long categoryId, Pageable pageable);
}
