-- 系统初始权限 --
INSERT IGNORE INTO tb_iam_permission(`id`, `identifier`, `name`, `description`, `sort_id`) VALUE
(200001, 'menu:iam', '“身份认证与访问管理”菜单', null, 200001),
(200002, 'menu:iam_user_manage', '“用户管理”菜单', null, 200002),
(200003, 'menu:iam_role_manage', '“角色管理”菜单', null, 200003),
(200004, 'menu:iam_permission_manage', '“权限管理”菜单', null, 200004);

-- 系统初始角色 --
INSERT IGNORE INTO tb_iam_role(`id`, `identifier`, `name`, `description`, `active`, `sort_id`) VALUE
(200001, 'super_admin', '超级管理员', null, 1, 200001);
INSERT IGNORE INTO tb_iam_role_permission(`role_id`, `permission_id`) VALUE
(200001, 200001),
(200001, 200002),
(200001, 200003),
(200001, 200004);

-- 系统初始用户 --
INSERT IGNORE INTO tb_iam_user(`id`, `username`, `password`, `salt`, `register_time`, `active`) VALUE
(200001, 'superadmin', '8176126c91f53981449d7575bafa1253', '6386718be1b3', '2025-01-01 00:00:00', 1);   -- superadmin / superadmin
INSERT IGNORE INTO tb_iam_profile(`user_id`, `avatar`, `nickname`, `photo`, `name`, `gender`, `employee_no`, `phone`, `email`, `main_department_id`, `main_position_id`) VALUE
(200001, null, '超级管理员', null, '超级管理员', null, null, null, null, null, null);
INSERT IGNORE INTO tb_iam_user_role(`user_id`, `role_id`) VALUE
(200001, 200001);   -- 超级管理员 <--> 超级管理员
