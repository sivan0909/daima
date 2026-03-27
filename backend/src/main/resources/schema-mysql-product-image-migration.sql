-- =====================================================
-- 商品图片迁移脚本（从旧字段 product.images TEXT 平滑迁移到 product_image 表）
-- 适用于已经线上运行、存在 product.images 字段的环境。
--
-- 建议步骤：
-- 1. 确认已经部署了包含 ProductImage 实体、product_image 表的版本
-- 2. 在业务低峰期执行本脚本
-- 3. 回填完成且验证无误后，再发版移除旧字段依赖（本仓库已删除 JPA 字段）
-- =====================================================

-- 1) 回填：将旧字段 images 中的 URL 迁移到 product_image 表
--    假设旧字段每行只有一条 URL；如有多条（逗号分隔），可根据实际情况拆分。
INSERT INTO product_image (product_id, url, sort_order)
SELECT id, images, 0
FROM product
WHERE images IS NOT NULL
  AND images <> '';

-- 2) 删除旧列：彻底移除 product.images TEXT 字段
ALTER TABLE product
  DROP COLUMN images;

