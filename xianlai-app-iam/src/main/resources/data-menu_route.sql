-- ###### SysMenu ##### --
INSERT IGNORE INTO tb_common_sys_menu(
    `id`, `sort_id`, `parent_id`, `icon`, `title`,
    `path_name`, `need_permission`, `permission`, `active`
) VALUE
(20001, 20001, 0, 'ri-group-fill', '身份认证和访问管理', 'IAM', 1, 'menu:iam', 1),
(20002, 20002, 20001, 'ri-user-settings-fill', '用户管理', 'IAM_USER_MANAGE', 1, 'menu:iam_user_manage', 1),
(20003, 20003, 20001, 'ri-account-box-fill', '角色管理', 'IAM_ROLE_MANAGE', 1, 'menu:iam_role_manage', 1),
(20004, 20004, 20001, 'ri-shield-keyhole-fill', '权限管理', 'IAM_PERMISSION_MANAGE', 1, 'menu:iam_permission_manage', 1);


-- ###### SysRoute ##### --
INSERT IGNORE INTO tb_common_sys_route(
    `id`, `sort_id`, `parent_id`, `name`, `path_name`, `redirect_path_name`, `component_path`,
    `need_login`, `need_permission`, `permission`, `show_tag`, `tag_title`
) VALUE
(20001, 20001, 11001, 'iam', 'IAM', 'IAM_REDIRECT', 'layout/components/Placeholder/index.vue', 1, 1, 'menu:iam', 0, null),
(20002, 20002, 20001, 'iam_user_manage', 'IAM_USER_MANAGE', null, 'iam/user_manage/index.vue', 1, 1, 'menu:iam_user_manage', 1, '用户管理'),
(20003, 20003, 20001, 'iam_role_manage', 'IAM_ROLE_MANAGE', null, 'iam/role_manage/index.vue', 1, 1, 'menu:iam_role_manage', 1, '角色管理'),
(20004, 20004, 20001, 'iam_permission_manage', 'IAM_PERMISSION_MANAGE', null, 'iam/permission_manage/index.vue', 1, 1, 'menu:iam_permission_manage', 1, '权限管理');
