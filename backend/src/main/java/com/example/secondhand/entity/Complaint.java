package com.example.secondhand.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "complaint")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "complaint_no", unique = true, length = 32)
    private String complaintNo;

    @Column(name = "refund_no", length = 64)
    private String refundNo;

    @Column(name = "order_no", length = 64)
    private String orderNo;

    @Column(name = "complainant_id")
    private Long complainantId;

    @Column(name = "complainant_name", length = 64)
    private String complainantName;

    @Column(name = "complained_id")
    private Long complainedId;

    @Column(name = "complained_name", length = 64)
    private String complainedName;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "complaint_type", length = 20)
    private String complaintType;

    @Column(length = 20)
    @Builder.Default
    private String status = "待处理";

    @Column(columnDefinition = "TEXT")
    private String result;

    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    public void prePersist() {
        if (createTime == null) createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (complaintNo == null || complaintNo.isEmpty()) {
            complaintNo = "CP" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()) + (int) (Math.random() * 1000);
        }
    }

    @PreUpdate
    public void preUpdate() {
        updateTime = LocalDateTime.now();
    }
}
