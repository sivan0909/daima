-- MySQL 表结构：用户表（管理员用户管理）
-- 若使用 MySQL，可执行此脚本创建表；使用 H2 时 JPA 会自动建表

CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE COMMENT '用户名',
    nickname VARCHAR(64) COMMENT '昵称',
    phone VARCHAR(20) COMMENT '手机号',
    password VARCHAR(128) NOT NULL COMMENT '密码(BCrypt)',
    role VARCHAR(20) DEFAULT 'buyer' COMMENT '角色：buyer/seller/admin',
    status VARCHAR(20) DEFAULT '正常' COMMENT '状态：正常/禁用',
    avatar VARCHAR(255) COMMENT '头像URL',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_login_time DATETIME NULL,
    INDEX idx_username (username),
    INDEX idx_role (role),
    INDEX idx_status (status)
) COMMENT '系统用户表';
