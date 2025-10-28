INSERT IGNORE INTO tb_common_sys_api(`id`, `call_path`, `description`, `request_method`, `url`) VALUE
-- User --
(20001, 'iam.user.register', '注册新用户', 'POST', '/api/iam/user/register'),
(20002, 'iam.user.login', '用户登录', 'POST', '/api/iam/user/login'),
(20003, 'iam.user.logout', '退出登录', 'GET', '/api/iam/user/logout'),
(20004, 'iam.user.changePassword', '修改密码', 'POST', '/api/iam/user/changePassword'),
(20005, 'iam.user.getPageConditionally', '条件查询用户分页', 'POST', '/api/iam/user/getPageConditionally'),
(20006, 'iam.user.bind', '为用户绑定/解除绑定角色', 'POST', '/api/iam/user/bind'),
(20007, 'iam.user.editUserInfo', '修改用户信息/注销用户', 'POST', '/api/iam/user/editUserInfo'),
-- Permission --
(21001, 'iam.permission.add', '新增权限', 'POST', '/api/iam/permission/add'),
(21002, 'iam.permission.delete', '删除权限', 'GET', '/api/iam/permission/delete'),
(21003, 'iam.permission.edit', '修改权限', 'POST', '/api/iam/permission/edit'),
(21004, 'iam.permission.getPageConditionally', '条件查询权限分页', 'POST', '/api/iam/permission/getPageConditionally'),
(21005, 'iam.permission.getRowNumStartFrom1', '查询权限的排名（从1开始）', 'GET', '/api/iam/permission/getRowNumStartFrom1'),
(21006, 'iam.permission.getPermissionIdsOfRole', '查询某角色所具有的权限ID列表', 'GET', '/api/iam/permission/getPermissionIdsOfRole'),
-- Role --
(22001, 'iam.role.add', '新增角色', 'POST', '/api/iam/role/add'),
(22002, 'iam.role.delete', '删除角色', 'GET', '/api/iam/role/delete'),
(22003, 'iam.role.edit', '修改角色', 'POST', '/api/iam/role/edit'),
(22004, 'iam.role.getPageConditionally', '条件查询角色分页', 'POST', '/api/iam/role/getPageConditionally'),
(22005, 'iam.role.getRowNumStartFrom1', '查询角色的排名（从1开始）', 'GET', '/api/iam/role/getRowNumStartFrom1'),
(22006, 'iam.role.getRoleIdsOfUser', '查询某用户所具有的角色ID列表', 'GET', '/api/iam/role/getRoleIdsOfUser'),
(22007, 'iam.role.grant', '为角色授权/解除授权', 'POST', '/api/iam/role/grant');
