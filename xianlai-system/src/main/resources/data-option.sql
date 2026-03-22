INSERT IGNORE INTO tb_common_sys_option(
    `id`, `sort_id`, `category`, `option_key`, `option_value`, `default_value`,
    `name`, `description`, `back_load`, `front_load`, `js_type`
) VALUE
-- ------- --
-- 其他控制 --
-- ------- --
-- 其他
(10001, 10001, 'other', 'request.timeout', '30000', '30000', '请求超时时长', '用于设置前端往后台发送API请求时，等待多长时间，超过这个时间报请求超时错误。单位：毫秒。注意：此参数值建议设置30000毫秒（30秒）以上，不应设置太小。', 0, 1, 'NUMBER'),
(10002, 10002, 'other', 'option.categoryList', '[{"label": "门户页面", "category": "portal"}, {"label": "注册登陆", "category": "user"}, {"label": "其他控制", "category": "other"}]', '[{"label": "门户页面", "category": "portal"}, {"label": "注册登陆", "category": "user"}, {"label": "其他控制", "category": "other"}]', '参数类别列表', '用于定义“系统设置-参数选项”页面有什么标签，以及每个标签所代表的参数分类标识符，在查询和新增参数时需要用该分类标识符进行区分。', 0, 1, 'ARRAY'),
-- 验证码
(11001, 11001, 'other', 'captcha.length', '5', '5', '验证码长度', '设置公用的验证码的长度，建议4～6位。', 1, 1, 'NUMBER'),
(11002, 11002, 'other', 'captcha.expireSeconds', '120', '120', '验证码有效期', '设置公用的验证码的有效期，单位：秒。', 1, 0, null),

-- ------- --
-- 门户页面 --
-- ------- --
-- 封面
(20001, 20001, 'portal', 'portal.coverImageType', 'local', 'local', '封面图片类型', '用于设置门户页面封面图片的类型，有这几种选择：local-前端工程内部本地图片 / upload-上传到附件库中的图片 / web-来源于网络的图片。', 0, 1, 'STRING'),
(20002, 20002, 'portal', 'portal.coverImagePath', '/images/portal/cover.jpg', '/images/portal/cover.jpg', '封面图片路径', '填写封面图片加载的路径。封面图片类型=local时，填写前端工程的本地图片的路径（图片存储在public目录下，路径以斜杠开头）；封面图片类型=upload时，填写上传图片的相对路径；封面图片类型=web时，填写网络图片的URL。', 0, 1, 'STRING'),
(20003, 20003, 'portal', 'portal.coverTitle', 'XianLai', 'XianLai', '封面标题', null, 0, 1, 'STRING'),
(20004, 20004, 'portal', 'portal.coverTitleSize', '5', '5', '封面标题字体大小', null, 0, 1, 'NUMBER'),
(20005, 20005, 'portal', 'portal.coverTitleColor', '#ffffff', '#ffffff', '封面标题字体颜色', '填写格式诸如“#ffffff”的十六进制颜色值。', 0, 1, 'STRING'),
(20006, 20006, 'portal', 'portal.coverSubTitle', '开源、轻量、配置式的多功能后台管理系统', '开源、轻量、配置式的多功能后台管理系统', '封面副标题', null, 0, 1, 'STRING'),
(20007, 20007, 'portal', 'portal.coverSubTitleSize', '2', '2', '封面副标题字体大小', null, 0, 1, 'NUMBER'),
(20008, 20008, 'portal', 'portal.coverSubTitleColor', '#ffffff', '#ffffff', '封面副标题字体颜色', '填写格式诸如“#ffffff”的十六进制颜色值。', 0, 1, 'STRING'),
-- 页脚
(21001, 21001, 'portal', 'portal.footerCopyright', '', '', '门户页脚版权声明', '在门户页脚显示诸如的“Copyright 2020-2025 xianlai.com”的版权声明小字。', 0, 1, 'STRING'),
(21002, 21002, 'portal', 'portal.footerBeianIcp', '', '', '门户页脚ICP备案号', '在门户页脚显示诸如的“粤ICP备11111111号-1”的工信部ICP备案号链接。', 0, 1, 'STRING'),
(21003, 21003, 'portal', 'portal.footerBeianGongan', '', '', '门户页脚公网安备号', '在门户页脚显示诸如的“粤公网安备11111111111111111号”的公安网络安全备案号链接。', 0, 1, 'STRING'),

-- ---------- --
-- 用户注册登陆 --
-- ---------- --
-- 注册开关
(30001, 30001, 'user', 'user.allowRegister', 'true', 'true', '允许注册', '是否允许用户自主注册，true表示允许，false表示禁止。允许时用户可以在门户页面进入到注册页面填写基本资料进行注册，否则只能由管理员在后台创建用户。', 1, 1, 'BOOLEAN'),
-- 用户名、密码格式
(31001, 31001, 'user', 'user.username.regexp', '^[a-zA-Z][a-zA-Z_0-9]{4,19}$', '^[a-zA-Z][a-zA-Z_0-9]{4,19}$', '用户名正则表达式', '用于规定用户名的格式。', 1, 1, 'STRING'),
(31002, 31002, 'user', 'user.username.tips', '仅限大写、小写字母，数字，下划线(_)，必须以字母开头', null, '用户名格式提示', '用于提示用户名应该输入什么样的内容，注意需要同用户名正则表达式相匹配。', 0, 1, 'STRING'),
(31003, 31003, 'user', 'user.username.minLen', '5', '5', '用户名最小长度', '最大取值不超过10，注意需要同用户名正则表达式相匹配。', 0, 1, 'NUMBER'),
(31004, 31004, 'user', 'user.username.maxLen', '20', '20', '用户名最大长度', '最大取值不超过100，注意需要同用户名正则表达式相匹配。', 0, 1, 'NUMBER'),
(31005, 31005, 'user', 'user.password.regexp', '^[a-zA-Z_0-9.~!@#$%^&*?]{6,30}$', '^[a-zA-Z_0-9.~!@#$%^&*?]{6,30}$', '密码正则表达式', '用于规定密码的格式。', 1, 1, 'STRING'),
(31006, 31006, 'user', 'user.password.tips', '仅限大写、小写字母，数字，下划线(_)，特殊字符(.~!@#$%^&*?)', null, '密码格式提示', '用于提示密码应该输入什么样的内容，注意需要同密码正则表达式相匹配。', 0, 1, 'STRING'),
(31007, 31007, 'user', 'user.password.minLen', '6', '6', '密码最小长度', '注意需要同密码正则表达式相匹配。', 0, 1, 'NUMBER'),
(31008, 31008, 'user', 'user.password.maxLen', '30', '30', '密码最大长度', '注意需要同密码正则表达式相匹配。', 0, 1, 'NUMBER'),
-- token令牌
(32001, 32001, 'user', 'token.timeout', '43200', '43200', '登录令牌过期时长', '用户成功登陆后，后台会发给token令牌，此参数设置令牌的过期时长。单位：秒，默认：12小时。', 1, 0, null),
(32002, 32002, 'user', 'token.activeTimeout', '10800', '10800', '登录令牌无操作失效时长', '用户成功登陆后，超过这段时间未进行任何操作，token令牌会失效。单位：秒，默认：3小时。', 1, 0, null);