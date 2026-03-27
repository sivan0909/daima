package com.example.secondhand.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "after_sale")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AfterSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "order_no", length = 30)
    private String orderNo;

    @Column(name = "user_id")
    private Long userId;

    @Column(length = 20)
    private String type;

    @Column(length = 20)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String result;

    @Column(name = "admin_id")
    private Long adminId;

    @Column(name = "admin_remark", length = 500)
    private String adminRemark;

    @Column(name = "handle_time")
    private LocalDateTime handleTime;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (updatedAt == null) updatedAt = LocalDateTime.now();
        if (status == null || status.isEmpty()) status = "待处理";
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

