INSERT IGNORE INTO tb_iam_permission(`id`, `sort_id`, `identifier`, `name`, `description`) VALUE
-- ------------- --
-- Common模块权限 --
-- ------------- --
-- 显示菜单
(10001, 10001, 'menu:iam', '“身份认证与访问管理”菜单', null),
(10002, 10002, 'menu:iam_user_manage', '“用户管理”菜单', null),
(10003, 10003, 'menu:iam_role_manage', '“角色管理”菜单', null),
(10004, 10004, 'menu:iam_permission_manage', '“权限管理”菜单', null),
(10101, 10101, 'menu:setting', '“系统设置”菜单', null),
(10102, 10102, 'menu:setting_path', '“路径常量”菜单', null),
(10103, 10103, 'menu:setting_menu', '“菜单管理”菜单', null),
(10104, 10104, 'menu:setting_route', '“路由配置”菜单', null),
(10105, 10105, 'menu:setting_option', '“参数选项”菜单', null),
(10106, 10106, 'menu:setting_api', '“后端接口”菜单', null),
-- 路径 Path
(11001, 11001, 'path:add', '添加路径', null),
(11002, 11002, 'path:delete', '删除路径', null),
(11003, 11003, 'path:edit', '修改路径', null),
(11004, 11004, 'path:query', '查询路径', null),
-- 接口 Api
(12001, 12001, 'api:add', '添加接口', null),
(12002, 12002, 'api:delete', '删除接口', null),
(12003, 12003, 'api:edit', '修改接口', null),
(12004, 12004, 'api:query', '查询接口', null),
-- 菜单 Menu
(13001, 13001, 'menu:add', '添加菜单', null),
(13002, 13002, 'menu:delete', '删除菜单', null),
(13003, 13003, 'menu:edit', '修改菜单', null),
(13004, 13004, 'menu:query', '查询菜单', null),
-- 路由 Route
(14001, 14001, 'route:add', '添加路由', null),
(14002, 14002, 'route:delete', '删除路由', null),
(14003, 14003, 'route:edit', '修改路由', null),
(14004, 14004, 'route:query', '查询路由', null),

-- ---------- --
-- IAM模块权限 --
-- ---------- --
-- 权限
(20001, 20001, 'permission:add', '添加权限', null),
(20002, 20002, 'permission:delete', '删除权限', null),
(20003, 20003, 'permission:edit', '修改权限', null),
(20004, 20004, 'permission:query', '查询权限', null),
-- 角色
(21001, 21001, 'role:add', '添加角色', null),
(21002, 21002, 'role:delete', '删除角色', null),
(21003, 21003, 'role:edit', '修改角色', null),
(21004, 21004, 'role:query', '查询角色', null),
(21005, 21005, 'role:grant', '角色授权', null),
-- 用户
(22001, 22001, 'user:add', '添加用户', null),
(22002, 22002, 'user:delete', '删除用户', null),
(22003, 22003, 'user:edit', '修改用户', null),
(22004, 22004, 'user:query', '查询用户', null),
(22005, 22005, 'user:bind', '绑定角色', null),
(22006, 22006, 'user:bind:super_admin', '角色绑定检查：super_admin', '具备此权限才能为用户绑定super_admin角色');
