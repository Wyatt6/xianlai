INSERT IGNORE INTO tb_sys_core_tenant(
       `id`, `code`, `domain`, `status`, `expire_time`, `logo`, `display_name`,
       `contact_name`, `contact_gender`, `contact_phone`, `contact_email`
) VALUES
(1, 'default', null, 'NORMAL', null, null, '默认租户', 'UNKNOWN', null, null, null);