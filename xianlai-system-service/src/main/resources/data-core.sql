/*
=======
  租户
=======
*/
INSERT IGNORE INTO tb_core_tenant(
       `id`, `code`, `domain`, `status`, `expire_time`, `logo`, `display_name`,
       `contact_name`, `contact_gender`, `contact_phone`, `contact_email`
) VALUES
(1, 'default', null, 'NORMAL', null, null, '默认租户', null, 'UNKNOWN', null, null);
-- 复位非初始化数据的自增主键初值为100000
ALTER TABLE tb_core_tenant AUTO_INCREMENT = 100000;


/*
===========
  系统配置
===========
*/
INSERT
IGNORE INTO tb_core_system_config(
    `id`, `enabled`, `scope`, `front_load`, `config_key`, `config_value`, `value_type`, `name`, `remark`
) VALUES
    (1, 1, 'SYSTEM', 0, 'system_config_update_time', current_timestamp(3), 'STRING', '系统配置最后更新时间', null),
    (2, 1, 'SYSTEM', 0, 'path_update_time', current_timestamp(3), 'STRING', '路径数据最后更新时间', null),
    (3, 1, 'SYSTEM', 0, 'route_update_time', current_timestamp(3), 'STRING', '路由数据最后更新时间', null),
-- 1XXX 是杂项配置
    (1001, 1, 'SYSTEM', 1, 'captcha.length', '5', 'INTEGER', '验证码长度', '设置系统所有验证码的长度（字符位数），建议4～6位，默认5位'),
    (1002, 1, 'SYSTEM', 0, 'captcha.expire_seconds', '120', 'INTEGER', '验证码有效期', '设置系统所有验证码的有效期，单位：秒，默认120秒'),
-- 2XXX 是用户管理、IAM等相关的配置
    (2001, 1, 'TENANT', 1, 'user.enable_register', 'true', 'BOOLEAN', '允许注册新用户', '是否允许新用户通过门户的“注册”按钮自主注册，true-允许 / false-禁止'),
    (2002, 1, 'TENANT', 0, 'user.token.timeout', '43200', 'LONG', '用户令牌过期时长', '用户登录成功后系统颁发的令牌有效期，单位：秒，默认：12小时'),
    (2003, 1, 'TENANT', 0, 'user.token.active_timeout', '10800', 'LONG', '用户无操作令牌失效时长', '用户登录成功后超过一段时间未进行任何操作，令牌自动失效，单位：秒，默认：3小时');
-- 复位非初始化数据的自增主键初值为100000
ALTER TABLE tb_core_system_config AUTO_INCREMENT = 100000;


/*
===========
  路径数据
===========
*/
INSERT
IGNORE INTO tb_core_path(
    `id`, `sort_id`, `name`, `path`
) VALUES
-- 门户页面路径
    (1001, 1001, 'PORTAL', '/portal'),
    (1002, 1002, 'LOGIN', '/portal/login'),
    (1003, 1003, 'REGISTER', '/portal/register'),
    (1004, 1004, 'RESET_PASSWORD', '/portal/reset-password'),
-- 错误页面路径
--     (99001, 9900000001, 'NOT_AUTHORIZED_EMBEDDED', '/403/embedded'),
--     (99002, 9900000002, 'NOT_AUTHORIZED', '/403'),
--     (99003, 9900000003, 'NOT_FOUND', '/404'),
--     (99004, 9900000004, 'NOT_CONNECTED', '/500'),
-- 最终无匹配路径
    (99999, 9999999999, 'FINAL_NOT_MATCH', '/:catchAll(.*)');
-- 复位非初始化数据的自增主键初值为100000
ALTER TABLE tb_core_path AUTO_INCREMENT = 100000;


/*
===========
  路由数据
===========
*/
INSERT
IGNORE INTO tb_core_route(
    `id`, `sort_id`, `parent_id`, `name`, `path_name`, `redirect_path_name`, `component_path`,
    `keep_alive`, `need_login`, `need_permission`, `permission`, `show_tag`, `tag_title`
) VALUES
-- 门户页面路由
    (1001, 1001, 0, 'portal', 'PORTAL', 'LOGIN', 'portal/index.vue', 0, 0, 0, null, 0, null),
    (1002, 1002, 1001, 'register', 'REGISTER', null, 'portal/register/index.vue', 0, 0, 0, null, 0, null),
    (1003, 1003, 1001, 'login', 'LOGIN', null, 'portal/login/index.vue', 0, 0, 0, null, 0, null),
    (1004, 1004, 1001, 'reset_password', 'RESET_PASSWORD', null, 'portal/reset_password/index.vue', 0, 0, 0, null, 0, null),
-- 错误页面路由
-- (99001, 9900000001, 11000001, 'not_authorized_embedded', 'NOT_AUTHORIZED_EMBEDDED', null, 'errors/403.vue', 0, 1, 0, null, 0, null),
    (99002, 9900000002, 0, 'not_authorized', 'NOT_AUTHORIZED', null, 'errors/403.vue', 0, 1, 0, null, 0, null),
    (99003, 9900000003, 0, 'not_found', 'NOT_FOUND', null, 'errors/404.vue', 0, 0, 0, null, 0, null),
    (99004, 9900000004, 0, 'not_connected', 'NOT_CONNECTED', null, 'errors/500.vue', 0, 0, 0, null, 0, null),
-- 最终无匹配路由
    (99999, 9999999999, 0, 'final_not_match', 'FINAL_NOT_MATCH', 'NOT_FOUND', null, 0, 0, 0, null, 0, null);
-- 复位非初始化数据的自增主键初值为100000
ALTER TABLE tb_core_route AUTO_INCREMENT = 100000;


/*
===========
  菜单数据
===========
*/
-- INSERT
-- IGNORE INTO tb_core_menu(
--     `id`, `sort_id`, `parent_id`, `icon`, `title`, `path_name`, `need_permission`, `permission`, `active`
-- ) VALUE
-- 首页
--     (1001, 1001, 0, 'ri-home-4-fill', '首页', 'HOMEPAGE', 0, null, 1);
-- 复位非初始化数据的自增主键初值为100000
ALTER TABLE tb_core_menu AUTO_INCREMENT = 100000;



-- -- ------------- --
-- -- Common模块菜单 --
-- -- ------------- --

-- -- 系统设置
-- (90001, 90001, 0, 'ri-settings-3-fill', '系统设置', 'SETTING', 1, "menu:setting", 1),
-- (90002, 90002, 90001, 'ri-character-recognition-fill', '路径常量', 'SETTING_PATH', 1, "menu:setting_path", 1),
-- (90003, 90003, 90001, 'ri-side-bar-fill', '菜单管理', 'SETTING_MENU', 1, "menu:setting_menu", 1),
-- (90004, 90004, 90001, 'ri-navigation-fill', '路由配置', 'SETTING_ROUTE', 1, "menu:setting_route", 1),
-- (90005, 90005, 90001, 'ri-file-check-fill', '参数选项', 'SETTING_OPTION', 1, "menu:setting_option", 1),
-- (90006, 90006, 90001, 'ri-puzzle-2-fill', '后端接口', 'SETTING_API', 1, "menu:setting_api", 1),
--
-- -- ---------- --
-- -- IAM模块菜单 --
-- -- ---------- --
-- (20001, 30001, 0, 'ri-group-fill', '身份认证和访问管理', 'IAM', 1, 'menu:iam', 1),
-- (20002, 30002, 20001, 'ri-user-settings-fill', '用户管理', 'IAM_USER_MANAGE', 1, 'menu:iam_user_manage', 1),
-- (20003, 30003, 20001, 'ri-account-box-fill', '角色管理', 'IAM_ROLE_MANAGE', 1, 'menu:iam_role_manage', 1),
-- (20004, 30004, 20001, 'ri-shield-keyhole-fill', '权限管理', 'IAM_PERMISSION_MANAGE', 1, 'menu:iam_permission_manage', 1),
--
-- -- -------------- --
-- -- ToolKit模组菜单 --
-- -- -------------- --
-- (30001, 20001, 0, 'ri-briefcase-4-fill', '多功能工具箱', 'TOOLKIT', 0, null, 1),
-- (30002, 20002, 30001, 'ri-file-shield-2-fill', '密码本', 'TOOLKIT_CODEBOOK', 0, null, 1);


-- INSERT IGNORE INTO tb_common_option_default(
--        `id`, `scope`, `option_key`, `default_value`, `value_type`, `name`, `description`, `front_load`
-- ) VALUE
-- (10002, 10002, 'other', 'XLOption.categoryList', '[{"label": "门户页面", "category": "portal"}, {"label": "注册登陆", "category": "user"}, {"label": "其他控制", "category": "other"}]', '[{"label": "门户页面", "category": "portal"}, {"label": "注册登陆", "category": "user"}, {"label": "其他控制", "category": "other"}]', '参数类别列表', '用于定义“系统设置-参数选项”页面有什么标签，以及每个标签所代表的参数分类标识符，在查询和新增参数时需要用该分类标识符进行区分。', 0, 1, 'ARRAY'),
-- -- ------- --
-- -- 门户页面 --
-- -- ------- --
-- -- 封面
-- (20001, 20001, 'portal', 'portal.coverImageType', 'local', 'local', '封面图片类型', '用于设置门户页面封面图片的类型，有这几种选择：local-前端工程内部本地图片 / upload-上传到附件库中的图片 / web-来源于网络的图片。', 0, 1, 'STRING'),
-- (20002, 20002, 'portal', 'portal.coverImagePath', '/images/portal/cover.jpg', '/images/portal/cover.jpg', '封面图片路径', '填写封面图片加载的路径。封面图片类型=local时，填写前端工程的本地图片的路径（图片存储在public目录下，路径以斜杠开头）；封面图片类型=upload时，填写上传图片的相对路径；封面图片类型=web时，填写网络图片的URL。', 0, 1, 'STRING'),
-- (20003, 20003, 'portal', 'portal.coverTitle', 'XianLai', 'XianLai', '封面标题', null, 0, 1, 'STRING'),
-- (20004, 20004, 'portal', 'portal.coverTitleSize', '5', '5', '封面标题字体大小', null, 0, 1, 'NUMBER'),
-- (20005, 20005, 'portal', 'portal.coverTitleColor', '#ffffff', '#ffffff', '封面标题字体颜色', '填写格式诸如“#ffffff”的十六进制颜色值。', 0, 1, 'STRING'),
-- (20006, 20006, 'portal', 'portal.coverSubTitle', '开源、轻量、配置式的多功能后台管理系统', '开源、轻量、配置式的多功能后台管理系统', '封面副标题', null, 0, 1, 'STRING'),
-- (20007, 20007, 'portal', 'portal.coverSubTitleSize', '2', '2', '封面副标题字体大小', null, 0, 1, 'NUMBER'),
-- (20008, 20008, 'portal', 'portal.coverSubTitleColor', '#ffffff', '#ffffff', '封面副标题字体颜色', '填写格式诸如“#ffffff”的十六进制颜色值。', 0, 1, 'STRING'),
-- -- 页脚
-- (21001, 21001, 'portal', 'portal.footerCopyright', '', '', '门户页脚版权声明', '在门户页脚显示诸如的“Copyright 2020-2025 xianlai.com”的版权声明小字。', 0, 1, 'STRING'),
-- (21002, 21002, 'portal', 'portal.footerBeianIcp', '', '', '门户页脚ICP备案号', '在门户页脚显示诸如的“粤ICP备11111111号-1”的工信部ICP备案号链接。', 0, 1, 'STRING'),
-- (21003, 21003, 'portal', 'portal.footerBeianGongan', '', '', '门户页脚公网安备号', '在门户页脚显示诸如的“粤公网安备11111111111111111号”的公安网络安全备案号链接。', 0, 1, 'STRING'),
--
-- -- ---------- --
-- -- 用户注册登陆 --
-- -- ---------- --
-- -- 注册开关
-- -- 用户名、密码格式
-- (31001, 31001, 'user', 'user.username.regexp', '^[a-zA-Z][a-zA-Z_0-9]{4,19}$', '^[a-zA-Z][a-zA-Z_0-9]{4,19}$', '用户名正则表达式', '用于规定用户名的格式。', 1, 1, 'STRING'),
-- (31002, 31002, 'user', 'user.username.tips', '仅限大写、小写字母，数字，下划线(_)，必须以字母开头', null, '用户名格式提示', '用于提示用户名应该输入什么样的内容，注意需要同用户名正则表达式相匹配。', 0, 1, 'STRING'),
-- (31003, 31003, 'user', 'user.username.minLen', '5', '5', '用户名最小长度', '最大取值不超过10，注意需要同用户名正则表达式相匹配。', 0, 1, 'NUMBER'),
-- (31004, 31004, 'user', 'user.username.maxLen', '20', '20', '用户名最大长度', '最大取值不超过100，注意需要同用户名正则表达式相匹配。', 0, 1, 'NUMBER'),
-- (31005, 31005, 'user', 'user.password.regexp', '^[a-zA-Z_0-9.~!@#$%^&*?]{6,30}$', '^[a-zA-Z_0-9.~!@#$%^&*?]{6,30}$', '密码正则表达式', '用于规定密码的格式。', 1, 1, 'STRING'),
-- (31006, 31006, 'user', 'user.password.tips', '仅限大写、小写字母，数字，下划线(_)，特殊字符(.~!@#$%^&*?)', null, '密码格式提示', '用于提示密码应该输入什么样的内容，注意需要同密码正则表达式相匹配。', 0, 1, 'STRING'),
-- (31007, 31007, 'user', 'user.password.minLen', '6', '6', '密码最小长度', '注意需要同密码正则表达式相匹配。', 0, 1, 'NUMBER'),
-- (31008, 31008, 'user', 'user.password.maxLen', '30', '30', '密码最大长度', '注意需要同密码正则表达式相匹配。', 0, 1, 'NUMBER'),


-- -- 首页、个人中心
-- (11001, 11001, 'INDEX', '/'),
-- (11002, 11002, 'INDEX_REDIRECT', '/homepage'),
-- (11003, 11003, 'HOMEPAGE', '/homepage'),
-- (11004, 11004, 'PROFILE', '/profile'),
-- -- 系统设置
-- (12001, 12001, 'SETTING', '/setting'),
-- (12002, 12002, 'SETTING_REDIRECT', '/setting/path'),
-- (12003, 12003, 'SETTING_PATH', '/setting/path'),
-- (12004, 12004, 'SETTING_MENU', '/setting/menu'),
-- (12005, 12005, 'SETTING_ROUTE', '/setting/route'),
-- (12006, 12006, 'SETTING_OPTION', '/setting/XLOption'),
-- (12007, 12007, 'SETTING_API', '/setting/api'),

-- -- ---------- --
-- -- IAM模块路径 --
-- -- ---------- --
-- (20001, 20001, 'IAM', '/iam'),
-- (20002, 20002, 'IAM_REDIRECT', '/iam/user-manage'),
-- (20003, 20003, 'IAM_USER_MANAGE', '/iam/user-manage'),
-- (20004, 20004, 'IAM_ROLE_MANAGE', '/iam/role-manage'),
-- (20005, 20005, 'IAM_PERMISSION_MANAGE', '/iam/permission-manage'),

-- -- -------------- --
-- -- Toolkit模组路径 --
-- -- -------------- --
-- (30001, 30001, 'TOOLKIT', '/toolkit'),
-- (30002, 30002, 'TOOLKIT_REDIRECT', '/toolkit/codebook'),
-- (30003, 30003, 'TOOLKIT_CODEBOOK', '/toolkit/codebook');


-- ------------- --
-- Common模块路由 --
-- ------------- --
-- -- -- 首页、个人中心
-- -- (11001, 11001, 0, 'layout', 'INDEX', 'INDEX_REDIRECT', 'layout/index.vue', 0, 1, 0, null, 0, null),
-- -- (11002, 11002, 11001, 'homepage', 'HOMEPAGE', null, 'homepage/index.vue', 1, 1, 0, null, 0, null),
-- -- (11003, 11003, 11001, 'profile', 'PROFILE', null, 'profile/index.vue', 1, 1, 0, null, 1, '个人中心'),
-- -- -- 系统设置
-- -- (90001, 90001, 11001, 'setting', 'SETTING', 'SETTING_REDIRECT', null, 0, 1, 1, "menu:setting", 0, null),
-- -- (90002, 90002, 11001, 'setting_path', 'SETTING_PATH', null, 'setting/path_manage/index.vue', 1, 1, 1, "menu:setting_path", 1, "路径常量"),
-- -- (90003, 90003, 11001, 'setting_menu', 'SETTING_MENU', null, 'setting/menu_manage/index.vue', 1, 1, 1, "menu:setting_menu", 1, "菜单管理"),
-- -- (90004, 90004, 11001, 'setting_route', 'SETTING_ROUTE', null, 'setting/route_manage/index.vue', 1, 1, 1, "menu:setting_route", 1, "路由配置"),
-- -- (90005, 90005, 11001, 'setting_option', 'SETTING_OPTION', null, 'setting/option_manage/index.vue', 1, 1, 1, "menu:setting_option", 1, "参数选项"),
-- -- (90006, 90006, 11001, 'setting_api', 'SETTING_API', null, 'setting/api_manage/index.vue', 1, 1, 1, "menu:setting_api", 1, "后台接口"),

-- -- ---------- --
-- -- IAM模块路由 --
-- -- ---------- --
-- -- (20001, 20001, 11001, 'iam', 'IAM', 'IAM_REDIRECT', null, 0, 1, 1, 'menu:iam', 0, null),
-- -- (20002, 20002, 11001, 'iam_user_manage', 'IAM_USER_MANAGE', null, 'iam/user_manage/index.vue', 1, 1, 1, 'menu:iam_user_manage', 1, '用户管理'),
-- -- (20003, 20003, 11001, 'iam_role_manage', 'IAM_ROLE_MANAGE', null, 'iam/role_manage/index.vue', 1, 1, 1, 'menu:iam_role_manage', 1, '角色管理'),
-- -- (20004, 20004, 11001, 'iam_permission_manage', 'IAM_PERMISSION_MANAGE', null, 'iam/permission_manage/index.vue', 1, 1, 1, 'menu:iam_permission_manage', 1, '权限管理'),
-- --
-- -- -- -------------- --
-- -- -- Toolkit模组路由 --
-- -- -- -------------- --
-- -- (30001, 30001, 11001, 'toolkit', 'TOOLKIT', 'TOOLKIT_REDIRECT', null, 0, 1, 0, null, 0, null),
-- -- (30002, 30002, 11001, 'toolkit_codebook', 'TOOLKIT_CODEBOOK', null, 'toolkit/codebook/index.vue', 1, 1, 0, null, 1, '密码本');
