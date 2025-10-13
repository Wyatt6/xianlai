INSERT IGNORE INTO tb_common_sys_api(`id`, `call_path`, `description`, `request_method`, `url`) VALUE
-- User --
(200001, 'iam.user.register', '注册新用户', 'POST', '/api/iam/user/register'),
(200002, 'iam.user.login', '用户登录', 'POST', '/api/iam/user/login'),
(200003, 'iam.user.logout', '退出登录', 'GET', '/api/iam/user/logout'),
-- Permission --
(201001, 'iam.permission.getPermissionsByPage', '条件查询权限分页', 'POST', '/api/iam/permission/getPermissionsByPage'),
(201002, 'iam.permission.deletePermission', '删除权限', 'GET', '/api/iam/permission/deletePermission');
