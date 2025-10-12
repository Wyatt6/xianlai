INSERT IGNORE INTO tb_common_sys_option(`sort_id`, `back_load`, `front_load`, `js_type`, `option_key`, `option_value`, `default_value`, `name`, `description`) VALUE
-- token令牌 --
(200001, 1, 0, null, 'token.timeout', '43200', '43200', '登录令牌过期时长', '单位：秒'),
(200002, 1, 0, null, 'token.activeTimeout', '3600', '3600', '登录令牌无操作失效时长', '单位：秒'),
-- 用户 --
(200003, 0, 1, 'NUMBER', 'user.username.minLen', '5', '5', '用户名最小长度', '须符合正则表达式要求'),
(200004, 0, 1, 'NUMBER', 'user.username.maxLen', '20', '20', '用户名最大长度', '须符合正则表达式要求'),
(200005, 1, 1, 'STRING', 'user.username.regexp', '^[a-zA-Z][a-zA-Z_0-9]{4,19}$', '^[a-zA-Z][a-zA-Z_0-9]{4,19}$', '用户名正则表达式', null),
(200006, 0, 1, 'STRING', 'user.username.tips', '仅限大写、小写字母，数字，下划线(_)，必须以字母开头', null, '用户名格式提示', '须符合正则表达式要求'),
(200007, 0, 1, 'NUMBER', 'user.password.minLen', '6', '6', '密码最小长度', '须符合正则表达式要求'),
(200008, 0, 1, 'NUMBER', 'user.password.maxLen', '30', '30', '密码最大长度', '须符合正则表达式要求'),
(200009, 1, 1, 'STRING', 'user.password.regexp', '^[a-zA-Z_0-9.~!@#$%^&*?]{6,30}$', '^[a-zA-Z_0-9.~!@#$%^&*?]{6,30}$', '密码正则表达式', null),
(200010, 0, 1, 'STRING', 'user.password.tips', '仅限大写、小写字母，数字，下划线(_)，特殊字符(.~!@#$%^&*?)', null, '密码格式提示', '须符合正则表达式要求');
