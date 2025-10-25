-- ##### Permission ##### --
INSERT IGNORE INTO tb_iam_permission(`id`, `sort_id`, `identifier`, `name`, `description`) VALUE
-- 菜单/路由权限 --
(20001, 20001, 'menu:iam', '“身份认证与访问管理”菜单', null),
(20002, 20002, 'menu:iam_user_manage', '“用户管理”菜单', null),
(20003, 20003, 'menu:iam_role_manage', '“角色管理”菜单', null),
(20004, 20004, 'menu:iam_permission_manage', '“权限管理”菜单', null),
-- 权限 --
(21001, 21001, 'permission:add', '添加权限', null),
(21002, 21002, 'permission:delete', '删除权限', null),
(21003, 21003, 'permission:edit', '修改权限', null),
(21004, 21004, 'permission:query', '查询权限', null),
-- 角色 --
(22001, 22001, 'role:add', '添加角色', null),
(22002, 22002, 'role:delete', '删除角色', null),
(22003, 22003, 'role:edit', '修改角色', null),
(22004, 22004, 'role:query', '查询角色', null),
(22005, 22005, 'role:grant', '角色授权', null),
-- 用户 --
(23001, 23001, 'user:add', '添加用户', null),
(23002, 23002, 'user:delete', '删除用户', null),
(23003, 23003, 'user:edit', '修改用户', null),
(23004, 23004, 'user:query', '查询用户', null),
(23005, 23005, 'user:bind', '绑定角色', null),
(23006, 23006, 'user:bind:super_admin', '绑定具体角色的权限：super_admin', null);


-- ##### Role ##### --
INSERT IGNORE INTO tb_iam_role(`id`, `sort_id`, `identifier`, `name`, `description`, `active`, `bind_check`) VALUE
(20001, 20001, 'super_admin', '超级管理员', null, 1, 1);
INSERT IGNORE INTO tb_iam_role_permission(`role_id`, `permission_id`) VALUE
-- 菜单/路由权限 --
(20001, 20001),
(20001, 20002),
(20001, 20003),
(20001, 20004),
-- 权限 --
(20001, 21001),
(20001, 21002),
(20001, 21003),
(20001, 21004),
-- 角色 --
(20001, 22001),
(20001, 22002),
(20001, 22003),
(20001, 22004),
(20001, 22005),
-- 用户 --
(20001, 23001),
(20001, 23002),
(20001, 23003),
(20001, 23004),
(20001, 23005),
(20001, 23006);


-- ##### User ##### --
INSERT IGNORE INTO tb_iam_user(`id`, `username`, `password`, `salt`, `register_time`, `active`) VALUE
-- superadmin / superadmin
(20001, 'superadmin', '8176126c91f53981449d7575bafa1253', '6386718be1b3', '2025-01-01 00:00:00', 1);
INSERT IGNORE INTO tb_iam_user_role(`user_id`, `role_id`) VALUE
-- superadmin
(20001, 20001);
