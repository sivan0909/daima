-- 商品/用户评价表（基于订单）
CREATE TABLE IF NOT EXISTS evaluation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    from_user_id BIGINT NOT NULL,
    to_user_id BIGINT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    content TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_eval_order (order_id),
    INDEX idx_eval_from_user (from_user_id),
    INDEX idx_eval_to_user (to_user_id),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (from_user_id) REFERENCES users(id),
    FOREIGN KEY (to_user_id) REFERENCES users(id)
);

