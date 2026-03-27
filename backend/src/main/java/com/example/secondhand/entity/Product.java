package com.example.secondhand.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, unique = true)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "original_price", precision = 10, scale = 2)
    private BigDecimal originalPrice;

    @Column(name = "sales", nullable = false)
    @Builder.Default
    private Integer sales = 0;

    @Column(name = "stock", nullable = false)
    @Builder.Default
    private Integer stock = 0;

    @Column(name = "`condition`", length = 50)
    @Builder.Default
    private String condition = "九成新";

    @Column(length = 20)
    @Builder.Default
    private String status = "在售";

    @Column(name = "seller_id")
    private Long sellerId;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "audit_status", length = 20)
    @Builder.Default
    private String auditStatus = "已通过";

    @Column(name = "publish_date")
    private LocalDate publishDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("sortOrder ASC, id ASC")
    @Builder.Default
    private List<ProductImage> productImages = new ArrayList<>();

    @Transient
    @JsonIgnore
    private List<String> incomingImageUrls;

    @JsonProperty("images")
    public List<String> getImages() {
        if (productImages == null || productImages.isEmpty()) {
            return List.of();
        }
        return productImages.stream()
                .map(ProductImage::getUrl)
                .filter(u -> u != null && !u.isBlank())
                .toList();
    }

    @JsonProperty("images")
    public void setImages(List<String> urls) {
        this.incomingImageUrls = urls;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (updatedAt == null) updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
