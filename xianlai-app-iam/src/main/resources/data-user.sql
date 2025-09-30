INSERT IGNORE INTO tb_common_sys_option(`sort_id`, `back_load`, `front_load`, `js_type`, `option_key`, `option_value`, `default_value`, `name`, `description`) VALUE
(1, 1, 1, 'STRING', 'user.username.regexp', '^[a-zA-Z][a-zA-Z_0-9]{4,19}$', '^[a-zA-Z][a-zA-Z_0-9]{4,19}$', '用户名正则表达式', null),
(1, 1, 1, 'STRING', 'user.password.regexp', '^[a-zA-Z_0-9.~!@#$%^&*?]{6,30}$', '^[a-zA-Z_0-9.~!@#$%^&*?]{6,30}$', '密码正则表达式', null);
