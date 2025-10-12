INSERT IGNORE INTO tb_common_sys_menu(`id`, `sort_id`, `icon`, `title`, `path_name`, `permission`, `active`, `parent_id`) VALUE
(200001, 200001, 'ri-group-fill', '身份认证和访问管理', 'IAM', null, 1, 0),
(200002, 200002, 'ri-user-settings-fill', '用户管理', 'IAM_USER_MANAGE', null, 1, 200001),
(200003, 200003, 'ri-account-box-fill', '角色管理', 'IAM_ROLE_MANAGE', null, 1, 200001),
(200004, 200004, 'ri-shield-keyhole-fill', '权限管理', 'IAM_PERMISSION_MANAGE', null, 1, 200001);
