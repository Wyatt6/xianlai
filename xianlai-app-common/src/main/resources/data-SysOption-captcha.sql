INSERT IGNORE INTO tb_common_sys_option(`sort_id`, `back_load`, `front_load`, `js_type`, `option_key`, `option_value`, `default_value`, `name`, `description`) VALUE
(1, 1, 1, 'NUMBER', 'captcha.length', '5', '5', '验证码长度', '推荐4～6位'),
(1, 1, 0, null, 'captcha.expireSeconds', '60', '60', '验证码有效期', '单位：秒');
