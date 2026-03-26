INSERT IGNORE INTO tb_core_system_config(
       `id`, `scope`, `enabled`, `front_load`, `name`, `config_key`, `config_value`, `value_type`, `remark`
) VALUES
       (1, 'SYSTEM', 1, 0, '系统配置最后更新时间', 'system_config_update_time', 0, 'LONG', '使用时间戳的形式表示');
-- 复位非初始化数据的自增主键初值为100000
ALTER TABLE tb_core_system_config AUTO_INCREMENT = 100000;
