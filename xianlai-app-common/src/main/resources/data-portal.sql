INSERT IGNORE INTO tb_common_sys_option(`sort_id`, `back_load`, `front_load`, `js_type`, `option_key`, `option_value`, `default_value`, `name`, `description`) VALUE
(1, 0, 1, 'STRING', 'portal.coverImageType', 'local', 'local', '封面图片类型', 'local-工程本地图片/upload-上传图片/web-网络图片'),
(1, 0, 1, 'STRING', 'portal.coverImagePath', '/src/assets/images/portal/default-cover.jpg', '/src/assets/images/portal/default-cover.jpg', '封面图片路径', '当coverImageType取值为local时，填写工程本地图片存储路径；当coverImageType取值为web时，填写网络图片URL'),
(1, 0, 1, 'STRING', 'portal.coverTitle', 'XianLai', 'XianLai', '封面标题', null),
(1, 0, 1, 'NUMBER', 'portal.coverTitleSize', '5', '5', '封面标题字体大小', null),
(1, 0, 1, 'STRING', 'portal.coverTitleColor', '#ffffff', '#ffffff', '封面标题字体颜色', '十六进制颜色值'),
(1, 0, 1, 'STRING', 'portal.coverSubTitle', '一款开源、轻量、配置式的多功能后台管理系统', '一款开源、轻量、配置式的多功能后台管理系统', '封面副标题', null),
(1, 0, 1, 'NUMBER', 'portal.coverSubTitleSize', '2', '2', '封面副标题字体大小', null),
(1, 0, 1, 'STRING', 'portal.coverSubTitleColor', '#ffffff', '#ffffff', '封面副标题字体颜色', '十六进制颜色值'),
(1, 0, 1, 'STRING', 'portal.footerCopyright', '', '', '门户页脚版权声明', '十六进制颜色值'),
(1, 0, 1, 'STRING', 'portal.footerBeianIcp', '', '', '门户页脚ICP备案号', null),
(1, 0, 1, 'STRING', 'portal.footerBeianGongan', '', '', '门户页脚公网安备号', null);
