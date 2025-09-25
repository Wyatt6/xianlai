INSERT IGNORE INTO tb_common_sys_option(`sort_id`, `front_load`, `js_type`, `back_load`, `java_type`, `option_key`, `option_value`, `default_value`, `name`, `description`) VALUE
(1, 1, 'NUMBER', 1, 'INT', 'captcha.length', '5', '5', '验证码长度', '推荐4～6位'),
(1, 0, null, 1, 'LONG', 'captcha.expireSeconds', '60', '60', '验证码有效期', '单位：秒');
