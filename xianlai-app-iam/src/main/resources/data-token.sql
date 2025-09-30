INSERT IGNORE INTO tb_common_sys_option(`sort_id`, `back_load`, `front_load`, `js_type`, `option_key`, `option_value`, `default_value`, `name`, `description`) VALUE
(1, 1, 0, null, 'token.timeout', '43200', '43200', '登录令牌过期时长', '单位：秒'),
(1, 1, 0, null, 'token.activeTimeout', '3600', '3600', '登录令牌无操作失效时长', '单位：秒');
