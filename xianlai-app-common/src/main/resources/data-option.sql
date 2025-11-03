INSERT IGNORE INTO tb_common_sys_option(
    `id`, `sort_id`, `option_key`, `option_value`, `default_value`,
    `name`, `description`, `back_load`, `front_load`, `js_type`
) VALUE
-- 其他 --
(10001, 10001, 'console.openLog', 'true', 'true', '控制台日志打印开关', 'true/false', 0, 1, 'BOOLEAN'),
(10002, 10002, 'request.timeout', '10000', '10000', '请求超时时长', '单位：毫秒', 0, 1, 'NUMBER'),
-- 验证码 --
(11001, 11001, 'captcha.length', '5', '5', '验证码长度', '建议4～6位', 1, 1, 'NUMBER'),
(11002, 11002, 'captcha.expireSeconds', '60', '60', '验证码有效期', '单位：秒', 1, 0, null),
-- 门户 --
(12001, 12001, 'portal.coverImageType', 'local', 'local', '封面图片类型', 'local-工程本地图片/upload-上传图片/web-网络图片', 0, 1, 'STRING'),
(12002, 12002, 'portal.coverImagePath', '/src/assets/images/portal/default-cover.jpg', '/src/assets/images/portal/default-cover.jpg', '封面图片路径', '当coverImageType取值为local时，填写工程本地图片路径；当coverImageType取值为upload时，填写上传图片的相对路径；当coverImageType取值为web时，填写网络图片URL', 0, 1, 'STRING'),
(12003, 12003, 'portal.coverTitle', 'XianLai', 'XianLai', '封面标题', null, 0, 1, 'STRING'),
(12004, 12004, 'portal.coverTitleSize', '5', '5', '封面标题字体大小', null, 0, 1, 'NUMBER'),
(12005, 12005, 'portal.coverTitleColor', '#ffffff', '#ffffff', '封面标题字体颜色', '十六进制颜色值', 0, 1, 'STRING'),
(12006, 12006, 'portal.coverSubTitle', '开源、轻量、配置式的多功能后台管理系统', '开源、轻量、配置式的多功能后台管理系统', '封面副标题', null, 0, 1, 'STRING'),
(12007, 12007, 'portal.coverSubTitleSize', '2', '2', '封面副标题字体大小', null, 0, 1, 'NUMBER'),
(12008, 12008, 'portal.coverSubTitleColor', '#ffffff', '#ffffff', '封面副标题字体颜色', '十六进制颜色值', 0, 1, 'STRING'),
(12009, 12009, 'portal.allowRegister', 'true', 'true', '允许注册', 'true/false', 1, 1, 'BOOLEAN'),
(12010, 12010, 'portal.footerCopyright', '', '', '门户页脚版权声明', null, 0, 1, 'STRING'),
(12011, 12011, 'portal.footerBeianIcp', '', '', '门户页脚ICP备案号', null, 0, 1, 'STRING'),
(12012, 12012, 'portal.footerBeianGongan', '', '', '门户页脚公网安备号', null, 0, 1, 'STRING'),

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

