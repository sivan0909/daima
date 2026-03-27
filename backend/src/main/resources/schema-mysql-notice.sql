-- MySQL 表结构：公告表（管理员公告管理）
-- 若使用 MySQL，可执行此脚本创建表；使用 H2 时 JPA 会自动建表

CREATE TABLE IF NOT EXISTS notice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL COMMENT '公告标题',
    content TEXT COMMENT '公告内容',
    is_top TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否置顶：0否 1是',
    status VARCHAR(20) DEFAULT '草稿' COMMENT '状态：已发布/草稿',
    publish_time DATETIME NULL COMMENT '发布时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_title (title(50)),
    INDEX idx_status (status),
    INDEX idx_publish_time (publish_time)
) COMMENT '平台公告表';
