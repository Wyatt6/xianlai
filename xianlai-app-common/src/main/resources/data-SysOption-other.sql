INSERT IGNORE INTO tb_common_sys_option(`sort_id`, `back_load`, `front_load`, `js_type`, `option_key`, `option_value`, `default_value`, `name`, `description`) VALUE
(1, 0, 1, 'BOOLEAN', 'console.openLog', 'true', 'true', '控制台日志打印开关', 'true/false'),
(1, 0, 1, 'NUMBER', 'request.timeout', '10000', '10000', '请求超时时长', '单位：毫秒');
