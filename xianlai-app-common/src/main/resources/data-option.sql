INSERT IGNORE INTO tb_common_sys_option(
    `id`, sort_id`, `option_key`, `option_value`, `default_value`,
    `name`, `description`, `back_load`, `front_load`, `js_type`
) VALUE
-- 验证码 --
(1, 1, 'captcha.length', '5', '5', '验证码长度', '建议4～6位', 1, 1, 'NUMBER' ),
(2, 2, 'captcha.expireSeconds', '60', '60', '验证码有效期', '单位：秒', 1, 0, null);
