-- =====================================================
-- 校园二手交易平台数据库初始化脚本
-- 包含：数据库创建、表结构、初始数据
-- =====================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS secondhand_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE secondhand_platform;

-- =====================================================
-- 表结构定义
-- =====================================================

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    school VARCHAR(100) COMMENT '学校',
    dormitory VARCHAR(200) COMMENT '宿舍',
    balance DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '余额',
    address VARCHAR(255) COMMENT '地址',
    role VARCHAR(20) DEFAULT 'buyer' COMMENT '角色',
    status VARCHAR(20) DEFAULT '正常' COMMENT '状态',
    avatar VARCHAR(255) DEFAULT '' COMMENT '头像',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 分类表
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(20) NOT NULL UNIQUE COMMENT '分类编号',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '分类名称',
    icon VARCHAR(20) COMMENT '图标',
    sort INT DEFAULT 1 COMMENT '排序',
    status ENUM('启用', '禁用') DEFAULT '启用' COMMENT '状态',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_code (code),
    INDEX idx_sort (sort)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

-- 商品表
CREATE TABLE IF NOT EXISTS products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(20) UNIQUE COMMENT '商品编号',
    name VARCHAR(100) NOT NULL COMMENT '商品名称',
    category_id BIGINT COMMENT '分类ID',
    price DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '售价',
    original_price DECIMAL(10,2) DEFAULT 0 COMMENT '原价',
    sales INT NOT NULL DEFAULT 0 COMMENT '销量',
    stock INT NOT NULL DEFAULT 0 COMMENT '库存',
    `condition` VARCHAR(50) DEFAULT '九成新' COMMENT '成色',
    status VARCHAR(20) DEFAULT '在售' COMMENT '状态',
    seller_id BIGINT COMMENT '卖家ID',
    description TEXT COMMENT '描述',
    audit_status VARCHAR(20) DEFAULT '已通过' COMMENT '审核状态',
    publish_date DATE COMMENT '发布日期',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_product_category (category_id),
    INDEX idx_product_status (status),
    INDEX idx_product_seller (seller_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 商品图片表（与商品表一对多）
CREATE TABLE IF NOT EXISTS product_image (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL COMMENT '商品ID',
    url TEXT NOT NULL COMMENT '图片URL',
    sort_order INT DEFAULT 0 COMMENT '排序（小的在前）',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_product_id (product_id),
    INDEX idx_sort (sort_order),
    CONSTRAINT fk_product_image_product
      FOREIGN KEY (product_id) REFERENCES products(id)
      ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品图片表';

-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(32) COMMENT '订单号',
    product_id BIGINT COMMENT '商品ID',
    product_name VARCHAR(100) COMMENT '商品名称',
    buyer_id BIGINT COMMENT '买家ID',
    seller_id BIGINT COMMENT '卖家ID',
    amount DECIMAL(10,2) COMMENT '金额',
    status VARCHAR(20) COMMENT '状态',
    receiver_name VARCHAR(50) COMMENT '收货人姓名',
    receiver_phone VARCHAR(20) COMMENT '收货人电话',
    receiver_address VARCHAR(255) COMMENT '收货地址',
    create_date DATETIME COMMENT '创建日期',
    complete_date DATETIME COMMENT '完成日期',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_order_no (order_no),
    INDEX idx_order_buyer (buyer_id),
    INDEX idx_order_seller (seller_id),
    INDEX idx_order_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单详情表（多商品订单明细）
CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT COMMENT '商品ID',
    product_name VARCHAR(100) COMMENT '商品名称',
    product_price DECIMAL(10,2) COMMENT '商品价格',
    quantity INT COMMENT '数量',
    subtotal DECIMAL(10,2) COMMENT '小计',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_order_items_order (order_id),
    CONSTRAINT fk_order_items_order
      FOREIGN KEY (order_id) REFERENCES orders(id)
      ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单详情表';

-- 购物车表
CREATE TABLE IF NOT EXISTS cart_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT COMMENT '用户ID',
    product_id BIGINT COMMENT '商品ID',
    quantity INT COMMENT '数量',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_cart_user (user_id),
    INDEX idx_cart_product (product_id),
    UNIQUE KEY uk_cart_user_product (user_id, product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 公告表
CREATE TABLE IF NOT EXISTS notice (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content TEXT COMMENT '内容',
    is_top TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否置顶',
    status ENUM('草稿', '已发布') DEFAULT '草稿' COMMENT '状态',
    publish_time DATETIME COMMENT '发布时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_notice_status (status),
    INDEX idx_notice_is_top (is_top)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- 投诉表
CREATE TABLE IF NOT EXISTS complaint (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    complaint_no VARCHAR(32) UNIQUE COMMENT '投诉编号',
    refund_no VARCHAR(64) COMMENT '退款编号',
    order_no VARCHAR(64) COMMENT '订单编号',
    complainant_id BIGINT COMMENT '投诉人ID',
    complainant_name VARCHAR(64) COMMENT '投诉人姓名',
    complained_id BIGINT COMMENT '被投诉人ID',
    complained_name VARCHAR(64) COMMENT '被投诉人姓名',
    reason TEXT COMMENT '投诉原因',
    complaint_type VARCHAR(20) COMMENT '投诉类型',
    status ENUM('待处理', '处理中', '已完成', '已拒绝') DEFAULT '待处理' COMMENT '状态',
    result TEXT COMMENT '处理结果',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_complaint_no (complaint_no),
    INDEX idx_complaint_order_no (order_no),
    INDEX idx_complaint_status (status),
    INDEX idx_complaint_complainant (complainant_id),
    INDEX idx_complaint_complained (complained_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投诉表';

-- 评价表
CREATE TABLE IF NOT EXISTS evaluation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL COMMENT '订单ID',
    from_user_id BIGINT NOT NULL COMMENT '评价用户ID',
    to_user_id BIGINT COMMENT '被评价用户ID',
    rating INT NOT NULL COMMENT '评分',
    content TEXT COMMENT '评价内容',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_evaluation_order (order_id),
    INDEX idx_evaluation_from_user (from_user_id),
    INDEX idx_evaluation_to_user (to_user_id),
    CONSTRAINT fk_evaluation_order
      FOREIGN KEY (order_id) REFERENCES orders(id)
      ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';


-- =====================================================
-- 初始化数据
-- =====================================================

-- 清空数据（按外键依赖顺序）
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE evaluation;
TRUNCATE TABLE complaint;
TRUNCATE TABLE notice;
TRUNCATE TABLE cart_items;
TRUNCATE TABLE order_items;
TRUNCATE TABLE orders;
TRUNCATE TABLE product_image;
TRUNCATE TABLE products;
TRUNCATE TABLE categories;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- 插入用户数据
INSERT INTO users (id, username, password, nickname, phone, email, school, dormitory, balance, address, role, status, avatar) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', '校园达人', '13800138000', 'admin@school.edu', 'XX大学', '1号楼101', 1000.00, 'XX大学1号楼', 'admin', '正常', ''),
(2, 'zhangsan', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', '张同学', '13800138001', 'zhangsan@school.edu', 'XX大学', '1号楼201', 500.00, 'XX大学1号楼', 'seller', '正常', ''),
(3, 'lisi', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', '李同学', '13800138002', 'lisi@school.edu', 'XX大学', '1号楼202', 300.00, 'XX大学1号楼', 'seller', '正常', ''),
(4, 'wangwu', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', '王同学', '13800138003', 'wangwu@school.edu', 'XX大学', '2号楼301', 800.00, 'XX大学2号楼', 'seller', '正常', ''),
(5, 'zhaoliu', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', '赵同学', '13800138004', 'zhaoliu@school.edu', 'XX大学', '2号楼302', 200.00, 'XX大学2号楼', 'seller', '正常', ''),
(6, 'liuqi', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', '刘同学', '13800138005', 'liuqi@school.edu', 'XX大学', '3号楼101', 600.00, 'XX大学3号楼', 'seller', '正常', ''),
(7, 'chenba', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', '陈同学', '13800138006', 'chenba@school.edu', 'XX大学', '3号楼102', 150.00, 'XX大学3号楼', 'seller', '正常', ''),
(8, 'zhoujiu', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', '周同学', '13800138007', 'zhoujiu@school.edu', 'XX大学', '4号楼201', 400.00, 'XX大学4号楼', 'seller', '正常', ''),
(9, 'wushi', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', '吴同学', '13800138008', 'wushi@school.edu', 'XX大学', '4号楼202', 250.00, 'XX大学4号楼', 'seller', '正常', ''),
(10, 'zhengyi', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', '郑同学', '13800138009', 'zhengyi@school.edu', 'XX大学', '5号楼301', 700.00, 'XX大学5号楼', 'seller', '正常', ''),
(11, 'suner', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', '孙同学', '13800138010', 'suner@school.edu', 'XX大学', '5号楼302', 100.00, 'XX大学5号楼', 'seller', '正常', ''),
(12, 'xiaoming', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', '小明', '13800138011', 'xiaoming@school.edu', 'XX大学', '6号楼101', 50.00, 'XX大学6号楼', 'buyer', '正常', ''),
(13, 'xiaohong', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', '小红', '13800138012', 'xiaohong@school.edu', 'XX大学', '6号楼102', 300.00, 'XX大学6号楼', 'buyer', '正常', ''),
(14, 'xiaohua', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', '小华', '13800138013', 'xiaohua@school.edu', 'XX大学', '7号楼201', 150.00, 'XX大学7号楼', 'buyer', '正常', ''),
(15, 'ttt', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', '买家用户', '13800138014', 'ttt@school.edu', 'XX大学', '7号楼202', 200.00, 'XX大学7号楼', 'buyer', '正常', ''),
(16, 'jjj', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', '卖家用户', '13800138015', 'jjj@school.edu', 'XX大学', '8号楼301', 500.00, 'XX大学8号楼', 'seller', '正常', '');

-- 插入分类数据
INSERT INTO categories (id, code, name, icon, sort, status) VALUES
(1, 'C001', '电子产品', 'phone', 1, '启用'),
(2, 'C002', '书籍教材', 'book', 2, '启用'),
(3, 'C003', '生活用品', 'home', 3, '启用'),
(4, 'C004', '服饰鞋包', 'shirt', 4, '启用'),
(5, 'C005', '运动户外', 'sport', 5, '启用'),
(6, 'C006', '其他', 'box', 6, '启用');

-- 插入商品数据
INSERT INTO products (id, code, name, category_id, price, original_price, sales, stock, `condition`, status, seller_id, description, audit_status, publish_date) VALUES
(1, 'P001', 'iPhone 13 Pro', 1, 4500.00, 7999.00, 5, 10, '九成新', '在售', 2, '自用一年，无磕碰', '已通过', '2024-12-01'),
(2, 'P002', '高等数学同济第七版', 2, 25.00, 45.00, 12, 20, '八成新', '在售', 3, '有少量笔记', '已通过', '2024-12-03'),
(3, 'P003', 'MacBook Air M1', 1, 5200.00, 7999.00, 8, 5, '九成新', '在售', 4, '配件齐全', '已通过', '2024-11-20'),
(4, 'P004', '小米台灯', 3, 50.00, 99.00, 3, 15, '全新', '在售', 5, '全新未拆封', '已通过', '2024-12-05'),
(5, 'P005', '耐克运动鞋 42码', 4, 280.00, 599.00, 6, 8, '八成新', '在售', 6, '穿过几次', '已通过', '2024-12-02'),
(6, 'P006', '线性代数教材', 2, 15.00, 35.00, 4, 12, '七成新', '在售', 7, '有划线标注', '已通过', '2024-12-04'),
(7, 'P007', '羽毛球拍套装', 5, 120.00, 200.00, 2, 6, '九成新', '在售', 8, '含3个球', '已通过', '2024-11-28'),
(8, 'P008', 'AirPods Pro 2', 1, 1200.00, 1899.00, 0, 3, '九成新', '下架', 9, '电池健康95%', '已通过', '2024-11-15'),
(9, 'P009', '宿舍收纳箱3件套', 3, 45.00, 89.00, 10, 25, '全新', '在售', 10, '买多了转让', '已通过', '2024-12-06'),
(10, 'P010', '考研英语真题', 2, 30.00, 68.00, 7, 18, '九成新', '在售', 11, '2024版', '已通过', '2024-12-01');

-- 插入商品图片数据（示例）
INSERT INTO product_image (product_id, url, sort_order) VALUES
(1, '/images/img02.png', 0),
(1, '/images/img03.png', 1),
(2, '/images/img04.png', 0),
(3, '/images/img05.png', 0),
(4, '/images/img06.png', 0),
(5, '/images/img07.png', 0),
(6, '/images/img08.png', 0),
(7, '/images/img09.png', 0),
(8, '/images/img10.png', 0),
(9, '/images/img11.png', 0),
(10, '/images/img12.png', 0);
