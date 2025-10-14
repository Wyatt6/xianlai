INSERT IGNORE INTO tb_common_sys_api(`id`, `call_path`, `description`, `request_method`, `url`) VALUE
-- User --
(200001, 'iam.user.register', '注册新用户', 'POST', '/api/iam/user/register'),
(200002, 'iam.user.login', '用户登录', 'POST', '/api/iam/user/login'),
(200003, 'iam.user.logout', '退出登录', 'GET', '/api/iam/user/logout'),
-- Permission --
(201001, 'iam.permission.getPermissionsByPage', '条件查询权限分页', 'POST', '/api/iam/permission/getPermissionsByPage'),
(201002, 'iam.permission.deletePermission', '删除权限', 'GET', '/api/iam/permission/deletePermission'),
(201003, 'iam.permission.addPermission', '新增权限', 'POST', '/api/iam/permission/addPermission'),
(201004, 'iam.permission.getRowNumStartFrom1', '查询权限的排名（从1开始）', 'GET', '/api/iam/permission/getRowNumStartFrom1'),
(201005, 'iam.permission.editPermission', '修改权限', 'POST', '/api/iam/permission/editPermission'),
-- Permission --
(202001, 'iam.role.getRolesByPage', '条件查询角色分页', 'POST', '/api/iam/role/getRolesByPage'),
(202002, 'iam.role.addRole', '新增角色', 'POST', '/api/iam/role/addRole'),
(202003, 'iam.role.getRowNumStartFrom1', '查询角色的排名（从1开始）', 'GET', '/api/iam/role/getRowNumStartFrom1');
