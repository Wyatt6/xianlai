INSERT IGNORE INTO tb_core_tenant(
       `id`, `code`, `domain`, `status`, `expire_time`, `logo`, `display_name`,
       `contact_name`, `contact_gender`, `contact_phone`, `contact_email`
) VALUES
(1, 'default', null, 'NORMAL', null, null, '默认租户', null, 'UNKNOWN', null, null);

-- 复位非初始化数据的自增主键初值为100000
ALTER TABLE tb_core_tenant AUTO_INCREMENT = 100000;