-- MySQL 表结构：投诉表（管理员投诉管理）
-- 若使用 MySQL，可执行此脚本创建表；使用 H2 时 JPA 会自动建表

CREATE TABLE IF NOT EXISTS complaint (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    complaint_no VARCHAR(32) UNIQUE COMMENT '投诉单号',
    refund_no VARCHAR(64) COMMENT '关联售后单号',
    order_no VARCHAR(64) COMMENT '订单号',
    complainant_id BIGINT COMMENT '投诉人ID',
    complainant_name VARCHAR(64) COMMENT '投诉人',
    complained_id BIGINT COMMENT '被投诉人ID',
    complained_name VARCHAR(64) COMMENT '被投诉人',
    reason TEXT COMMENT '投诉原因',
    complaint_type VARCHAR(20) COMMENT '投诉类型：退款/换货/其他',
    status VARCHAR(20) DEFAULT '待处理' COMMENT '状态：待处理/处理中/已完成',
    result TEXT COMMENT '仲裁结果',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_complaint_no (complaint_no),
    INDEX idx_order_no (order_no),
    INDEX idx_complainant (complainant_name(32)),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) COMMENT '投诉表';
