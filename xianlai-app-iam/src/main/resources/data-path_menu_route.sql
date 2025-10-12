INSERT IGNORE INTO tb_common_sys_path(`name`, `path`, `sort_id`) VALUE
('IAM', '/iam', 200001),
('IAM_REDIRECT', '/iam/user-manage', 200002),
('IAM_USER_MANAGE', '/iam/user-manage', 200003),
('IAM_ROLE_MANAGE', '/iam/role-manage', 200004),
('IAM_PERMISSION_MANAGE', '/iam/permission-manage', 200005);

INSERT IGNORE INTO tb_common_sys_menu(`id`, `sort_id`, `icon`, `title`, `path_name`, `permission`, `active`, `parent_id`) VALUE
(200001, 200001, 'ri-group-fill', '身份认证和访问管理', 'IAM', null, 1, 0),
(200002, 200002, 'ri-user-settings-fill', '用户管理', 'IAM_USER_MANAGE', null, 1, 200001),
(200003, 200003, 'ri-account-box-fill', '角色管理', 'IAM_ROLE_MANAGE', null, 1, 200001),
(200004, 200004, 'ri-shield-keyhole-fill', '权限管理', 'IAM_PERMISSION_MANAGE', null, 1, 200001);

INSERT IGNORE INTO tb_common_sys_route(`id`, `sort_id`, `name`, `path_name`, `redirect_path_name`, `component_path`, `need_login`, `need_permission`, `permission`, `show_tag`, `tag_title`, `parent_id`) VALUE
(200001, 200001, 'iam', 'IAM', 'IAM_REDIRECT', 'layout/components/Placeholder/index.vue', 1, 0, null, 0, null, 100005),
(200002, 200002, 'iam_user_manage', 'IAM_USER_MANAGE', null, 'iam/user_manage/index.vue', 1, 0, null, 1, '用户管理', 200001),
(200003, 200003, 'iam_role_manage', 'IAM_ROLE_MANAGE', null, 'iam/role_manage/index.vue', 1, 0, null, 1, '角色管理', 200001),
(200004, 200004, 'iam_permission_manage', 'IAM_PERMISSION_MANAGE', null, 'iam/permission_manage/index.vue', 1, 0, null, 1, '权限管理', 200001);
