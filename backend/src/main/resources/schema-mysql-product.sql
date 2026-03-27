-- MySQL 表结构：商品表（卖家商品管理）
-- 若使用 MySQL，可执行此脚本创建表；使用 H2 时 JPA 会自动建表

CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL COMMENT '商品名称',
    category VARCHAR(100) COMMENT '分类',
    price DECIMAL(10,2) NOT NULL COMMENT '售价',
    original_price DECIMAL(10,2) COMMENT '原价',
    `condition` VARCHAR(50) COMMENT '成色',
    stock INT DEFAULT 0 COMMENT '库存',
    description TEXT COMMENT '商品描述',
    images TEXT COMMENT '图片URL，多个用逗号分隔',
    status VARCHAR(20) DEFAULT '上架' COMMENT '状态：上架/下架',
    seller_id BIGINT NOT NULL COMMENT '卖家ID',
    seller_name VARCHAR(100) COMMENT '卖家名称',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_seller (seller_id)
) COMMENT '商品表';
