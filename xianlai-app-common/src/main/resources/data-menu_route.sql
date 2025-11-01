-- ###### SysMenu ##### --
INSERT IGNORE INTO tb_common_sys_menu(
    `id`, `sort_id`, `parent_id`, `icon`, `title`,
    `path_name`, `need_permission`, `permission`, `active`
) VALUE
-- 首页
(10001, 10001, 0, 'ri-home-4-fill', '首页', 'HOMEPAGE', 0, null, 1),
-- 系统设置
(90001, 90001, 0, 'ri-settings-3-fill', '系统设置', 'SETTING', 1, "menu:setting", 1),
(90002, 90002, 90001, 'ri-character-recognition-fill', '路径常量', 'SETTING_PATH', 1, "menu:setting_path", 1),
(90003, 90003, 90001, 'ri-side-bar-fill', '菜单管理', 'SETTING_MENU', 1, "menu:setting_menu", 1),
(90004, 90004, 90001, 'ri-navigation-fill', '路由配置', 'SETTING_ROUTE', 1, "menu:setting_route", 1),
(90005, 90005, 90001, 'ri-file-check-fill', '参数选项', 'SETTING_OPTION', 1, "menu:setting_option", 1),
(90006, 90006, 90001, 'ri-puzzle-2-fill', '后端接口', 'SETTING_API', 1, "menu:setting_api", 1);


-- ###### SysRoute ##### --
INSERT IGNORE INTO tb_common_sys_route(
    `id`, `sort_id`, `parent_id`, `name`, `path_name`, `redirect_path_name`, `component_path`,
    `need_login`, `need_permission`, `permission`, `show_tag`, `tag_title`
) VALUE
-- 门户 --
(10001, 10001, 0, 'portal', 'PORTAL', 'LOGIN', 'portal/index.vue', 0, 0, null, 0, null),
(10002, 10002, 10001, 'register', 'REGISTER', null, 'portal/register/index.vue', 0, 0, null, 0, null),
(10003, 10003, 10001, 'login', 'LOGIN', null, 'portal/login/index.vue', 0, 0, null, 0, null),
(10004, 10004, 10001, 'reset_password', 'RESET_PASSWORD', null, 'portal/reset_password/index.vue', 0, 0, null, 0, null),
-- 首页 --
(11001, 11001, 0, 'layout', 'INDEX', 'INDEX_REDIRECT', 'layout/index.vue', 1, 0, null, 0, null),
(11002, 11002, 11001, 'homepage', 'HOMEPAGE', null, 'homepage/index.vue', 1, 0, null, 0, null),
-- 系统设置 --
(90001, 90001, 11001, 'setting', 'SETTING', 'SETTING_REDIRECT', 'layout/components/Placeholder/index.vue', 1, 1, "menu:setting", 0, null),
(90002, 90002, 90001, 'setting_path', 'SETTING_PATH', null, 'setting/path_manage/index.vue', 1, 1, "menu:setting_path", 1, "路径常量"),
(90003, 90003, 90001, 'setting_menu', 'SETTING_MENU', null, 'setting/menu_manage/index.vue', 1, 1, "menu:setting_menu", 1, "菜单管理"),
(90004, 90004, 90001, 'setting_route', 'SETTING_ROUTE', null, 'setting/route_manage/index.vue', 1, 1, "menu:setting_route", 1, "路由配置"),
(90005, 90005, 90001, 'setting_option', 'SETTING_OPTION', null, 'setting/option_manage/index.vue', 1, 1, "menu:setting_option", 1, "参数选项"),
(90006, 90006, 90001, 'setting_api', 'SETTING_API', null, 'setting/api_manage/index.vue', 1, 1, "menu:setting_api", 1, "后台接口"),
-- 错误 --
(99001, 99001, 11001, 'not_authorized_embedded', 'NOT_AUTHORIZED_EMBEDDED', null, 'errors/403.vue', 1, 0, null, 0, null),
(99002, 99002, 0, 'not_authorized', 'NOT_AUTHORIZED', null, 'errors/403.vue', 1, 0, null, 0, null),
(99003, 99003, 0, 'not_found', 'NOT_FOUND', null, 'errors/404.vue', 0, 0, null, 0, null),
(99004, 99004, 0, 'not_connected', 'NOT_CONNECTED', null, 'errors/500.vue', 0, 0, null, 0, null),
-- 最终无匹配 --
(99999, 99999, 0, 'final_not_match', 'FINAL_NOT_MATCH', 'NOT_FOUND', null, 0, 0, null, 0, null);
