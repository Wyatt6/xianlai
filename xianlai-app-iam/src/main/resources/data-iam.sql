-- 系统初始权限 --
INSERT IGNORE INTO tb_iam_permission(`id`, `identifier`, `name`, `description`, `sort_id`) VALUE
-- 菜单/路由权限 --
(200001, 'menu:iam', '“身份认证与访问管理”菜单', null, 200001),
(200002, 'menu:iam_user_manage', '“用户管理”菜单', null, 200002),
(200003, 'menu:iam_role_manage', '“角色管理”菜单', null, 200003),
(200004, 'menu:iam_permission_manage', '“权限管理”菜单', null, 200004),
-- 权限 --
(201001, 'permission:add', '添加权限', null, 201001),
(201002, 'permission:delete', '删除权限', null, 201002),
(201003, 'permission:edit', '修改权限', null, 201003),
(201004, 'permission:query', '查询权限', null, 201004),
-- 角色 --
(202001, 'role:add', '添加角色', null, 202001),
(202002, 'role:delete', '删除角色', null, 202002),
(202003, 'role:edit', '修改角色', null, 202003),
(202004, 'role:query', '查询角色', null, 202004);

-- 系统初始角色 --
INSERT IGNORE INTO tb_iam_role(`id`, `identifier`, `name`, `description`, `active`, `sort_id`) VALUE
(200001, 'super_admin', '超级管理员', null, 1, 200001);
INSERT IGNORE INTO tb_iam_role_permission(`role_id`, `permission_id`) VALUE
-- 菜单/路由权限 --
(200001, 200001),
(200001, 200002),
(200001, 200003),
(200001, 200004),
-- 权限 --
(200001, 201001),
(200001, 201002),
(200001, 201003),
(200001, 201004),
-- 角色 --
(200001, 202001),
(200001, 202002),
(200001, 202003),
(200001, 202004);

-- 系统初始用户 --
INSERT IGNORE INTO tb_iam_user(`id`, `username`, `password`, `salt`, `register_time`, `active`) VALUE
(200001, 'superadmin', '8176126c91f53981449d7575bafa1253', '6386718be1b3', '2025-01-01 00:00:00', 1);   -- superadmin / superadmin
INSERT IGNORE INTO tb_iam_profile(`user_id`, `avatar`, `nickname`, `photo`, `name`, `gender`, `employee_no`, `phone`, `email`, `main_department_id`, `main_position_id`) VALUE
(200001, null, '超级管理员', null, '超级管理员', null, null, null, null, null, null);
INSERT IGNORE INTO tb_iam_user_role(`user_id`, `role_id`) VALUE
(200001, 200001);   -- 超级管理员 <--> 超级管理员
