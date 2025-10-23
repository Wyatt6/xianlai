INSERT IGNORE INTO tb_common_sys_api(`id`, `call_path`, `description`, `request_method`, `url`) VALUE
-- User --
(20001, 'iam.user.register', '注册新用户', 'POST', '/api/iam/user/register'),
(20002, 'iam.user.login', '用户登录', 'POST', '/api/iam/user/login'),
(20003, 'iam.user.logout', '退出登录', 'GET', '/api/iam/user/logout'),
-- Permission --
(21001, 'iam.permission.add', '新增权限', 'POST', '/api/iam/permission/add'),
(21002, 'iam.permission.delete', '删除权限', 'GET', '/api/iam/permission/delete'),
(21003, 'iam.permission.edit', '修改权限', 'POST', '/api/iam/permission/edit'),
(21004, 'iam.permission.getPageConditionally', '条件查询权限分页', 'POST', '/api/iam/permission/getPageConditionally'),
(21005, 'iam.permission.getRowNumStartFrom1', '查询权限的排名（从1开始）', 'GET', '/api/iam/permission/getRowNumStartFrom1'),
(21007, 'iam.permission.getPermissionIdsOfRole', '查询某角色所具有的权限ID列表', 'GET', '/api/iam/permission/getPermissionIdsOfRole'),
-- Role --
(22001, 'iam.role.getRolesByPage', '条件查询角色分页', 'POST', '/api/iam/role/getRolesByPage'),
(22002, 'iam.role.addRole', '新增角色', 'POST', '/api/iam/role/addRole'),
(22003, 'iam.role.getRowNumStartFrom1', '查询角色的排名（从1开始）', 'GET', '/api/iam/role/getRowNumStartFrom1'),
(22004, 'iam.role.deleteRole', '删除角色', 'GET', '/api/iam/role/deleteRole'),
(22005, 'iam.role.editRole', '修改角色', 'POST', '/api/iam/role/editRole'),
(22006, 'iam.role.updateGrants', '更新角色的授权', 'POST', '/api/iam/role/updateGrants');
