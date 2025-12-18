INSERT IGNORE INTO tb_common_sys_menu(
    `id`, `sort_id`, `parent_id`, `icon`, `title`,
    `path_name`, `need_permission`, `permission`, `active`
) VALUE
-- ------------- --
-- Common模块菜单 --
-- ------------- --
-- 首页
(10001, 10001, 0, 'ri-home-4-fill', '首页', 'HOMEPAGE', 0, null, 1),
-- 系统设置
(90001, 90001, 0, 'ri-settings-3-fill', '系统设置', 'SETTING', 1, "menu:setting", 1),
(90002, 90002, 90001, 'ri-character-recognition-fill', '路径常量', 'SETTING_PATH', 1, "menu:setting_path", 1),
(90003, 90003, 90001, 'ri-side-bar-fill', '菜单管理', 'SETTING_MENU', 1, "menu:setting_menu", 1),
(90004, 90004, 90001, 'ri-navigation-fill', '路由配置', 'SETTING_ROUTE', 1, "menu:setting_route", 1),
(90005, 90005, 90001, 'ri-file-check-fill', '参数选项', 'SETTING_OPTION', 1, "menu:setting_option", 1),
(90006, 90006, 90001, 'ri-puzzle-2-fill', '后端接口', 'SETTING_API', 1, "menu:setting_api", 1),

-- ---------- --
-- IAM模块菜单 --
-- ---------- --
(20001, 20001, 0, 'ri-group-fill', '身份认证和访问管理', 'IAM', 1, 'menu:iam', 1),
(20002, 20002, 20001, 'ri-user-settings-fill', '用户管理', 'IAM_USER_MANAGE', 1, 'menu:iam_user_manage', 1),
(20003, 20003, 20001, 'ri-account-box-fill', '角色管理', 'IAM_ROLE_MANAGE', 1, 'menu:iam_role_manage', 1),
(20004, 20004, 20001, 'ri-shield-keyhole-fill', '权限管理', 'IAM_PERMISSION_MANAGE', 1, 'menu:iam_permission_manage', 1),

-- -------------- --
-- ToolKit模组菜单 --
-- -------------- --
(30001, 30001, 0, 'ri-briefcase-4-fill', '多功能工具箱', 'TOOLKIT', 0, null, 1),
(30002, 30002, 30001, 'ri-file-shield-2-fill', '密码本', 'TOOLKIT_CODEBOOK', 0, null, 1);
