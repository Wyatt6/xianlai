INSERT IGNORE INTO tb_common_sys_menu(`id`, `sort_id`, `icon`, `title`, `path_name`, `permission`, `active`, `parent_id`) VALUE
(1, 1, 'ri-home-4-fill', '首页', 'HOMEPAGE', null, 1, 0),
(2, 2, 'ri-group-fill', '身份认证管理', 'IAM', null, 1, 0),
(3, 3, 'ri-user-settings-fill', '用户管理', 'IAM_USER_MANAGE', null, 1, 2),
(4, 4, 'ri-account-box-fill', '角色管理', 'IAM_ROLE_MANAGE', null, 1, 2),
(5, 5, 'ri-shield-keyhole-fill', '权限管理', 'IAM_PERMISSION_MANAGE', null, 1, 2);
