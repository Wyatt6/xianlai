INSERT IGNORE INTO tb_common_sys_option(
    `id`, `sort_id`, `option_key`, `option_value`, `default_value`,
    `name`, `description`, `back_load`, `front_load`, `js_type`
) VALUE
-- token令牌 --
(20001, 20001, 'token.timeout', '43200', '43200', '登录令牌过期时长', '单位：秒', 1, 0, null),
(20002, 20002, 'token.activeTimeout', '3600', '3600', '登录令牌无操作失效时长', '单位：秒', 1, 0, null),
-- 用户格式 --
(21001, 21001, 'user.username.minLen', '5', '5', '用户名最小长度', '最大取值不超过10，须符合正则表达式要求', 0, 1, 'NUMBER'),
(21002, 21002, 'user.username.maxLen', '20', '20', '用户名最大长度', '最大取值不超过100，须符合正则表达式要求', 0, 1, 'NUMBER'),
(21003, 21003, 'user.username.regexp', '^[a-zA-Z][a-zA-Z_0-9]{4,19}$', '^[a-zA-Z][a-zA-Z_0-9]{4,19}$', '用户名正则表达式', null, 1, 1, 'STRING'),
(21004, 21004, 'user.username.tips', '仅限大写、小写字母，数字，下划线(_)，必须以字母开头', null, '用户名格式提示', '须符合正则表达式要求', 0, 1, 'STRING'),
(21005, 21005, 'user.password.minLen', '6', '6', '密码最小长度', '须符合正则表达式要求', 0, 1, 'NUMBER'),
(21006, 21006, 'user.password.maxLen', '30', '30', '密码最大长度', '须符合正则表达式要求', 0, 1, 'NUMBER'),
(21007, 21007, 'user.password.regexp', '^[a-zA-Z_0-9.~!@#$%^&*?]{6,30}$', '^[a-zA-Z_0-9.~!@#$%^&*?]{6,30}$', '密码正则表达式', null, 1, 1, 'STRING'),
(21008, 21008, 'user.password.tips', '仅限大写、小写字母，数字，下划线(_)，特殊字符(.~!@#$%^&*?)', null, '密码格式提示', '须符合正则表达式要求', 0, 1, 'STRING');
