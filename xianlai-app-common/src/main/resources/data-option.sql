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
(12012, 12012, 'portal.footerBeianGongan', '', '', '门户页脚公网安备号', null, 0, 1, 'STRING');
