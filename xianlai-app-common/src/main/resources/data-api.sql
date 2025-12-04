INSERT IGNORE INTO tb_common_sys_api(`id`, `call_path`, `description`, `request_method`, `url`) VALUE
-- ------------- --
-- Common模块接口 --
-- ------------- --
-- InitController
(10001, 'common.init.getInitData', '获取初始化数据', 'GET', '/api/common/init/getInitData'),
-- CaptchaController
(11001, 'common.captcha.getCaptcha', '获取验证码', 'GET', '/api/common/captcha/getCaptcha'),
-- PathController
(12001, 'common.path.add', '新增路径', 'POST', '/api/common/path/add'),
(12002, 'common.path.delete', '删除路径', 'GET', '/api/common/path/delete'),
(12003, 'common.path.edit', '修改路径', 'POST', '/api/common/path/edit'),
(12004, 'common.path.reloadCache', '重载路径缓存', 'GET', '/api/common/path/reloadCache'),
(12005, 'common.path.getPageConditionally', '条件查询路径分页', 'POST', '/api/common/path/getPageConditionally'),
-- ApiController
(13001, 'common.api.add', '新增接口', 'POST', '/api/common/api/add'),
(13002, 'common.api.delete', '删除接口', 'GET', '/api/common/api/delete'),
(13003, 'common.api.edit', '修改接口', 'POST', '/api/common/api/edit'),
(13004, 'common.api.reloadCache', '重载接口缓存', 'GET', '/api/common/api/reloadCache'),
(13005, 'common.api.getPageConditionally', '条件查询接口分页', 'POST', '/api/common/api/getPageConditionally'),
-- MenuController
(14001, 'common.menu.add', '新增菜单', 'POST', '/api/common/menu/add'),
(14002, 'common.menu.delete', '删除菜单', 'GET', '/api/common/menu/delete'),
(14003, 'common.menu.edit', '修改菜单', 'POST', '/api/common/menu/edit'),
(14004, 'common.menu.reloadCache', '重载菜单缓存', 'GET', '/api/common/menu/reloadCache'),
(14005, 'common.menu.getForest', '查询菜单森林', 'GET', '/api/common/menu/getForest'),
-- RouteController
(15001, 'common.route.add', '新增路由', 'POST', '/api/common/route/add'),
(15002, 'common.route.delete', '删除路由', 'GET', '/api/common/route/delete'),
(15003, 'common.route.edit', '修改路由', 'POST', '/api/common/route/edit'),
(15004, 'common.route.reloadCache', '重载路由缓存', 'GET', '/api/common/route/reloadCache'),
(15005, 'common.route.getForest', '查询路由森林', 'GET', '/api/common/route/getForest'),
-- OptionController
(16001, 'common.option.add', '新增参数', 'POST', '/api/common/option/add'),
(16002, 'common.option.delete', '删除参数', 'GET', '/api/common/option/delete'),
(16003, 'common.option.edit', '修改参数', 'POST', '/api/common/option/edit'),
(16004, 'common.option.reloadCache', '重载参数缓存', 'GET', '/api/common/option/reloadCache'),
(16005, 'common.option.getClassifiedList', '查询分类后的参数列表', 'GET', '/api/common/option/getClassifiedList'),

-- ---------- --
-- IAM模块接口 --
-- ---------- --
-- PermissionController
(20001, 'iam.permission.add', '新增权限', 'POST', '/api/iam/permission/add'),
(20002, 'iam.permission.delete', '删除权限', 'GET', '/api/iam/permission/delete'),
(20003, 'iam.permission.edit', '修改权限', 'POST', '/api/iam/permission/edit'),
(20004, 'iam.permission.getPageConditionally', '条件查询权限分页', 'POST', '/api/iam/permission/getPageConditionally'),
(20005, 'iam.permission.getPermissionIdsOfRole', '查询某角色所具有的权限ID列表', 'GET', '/api/iam/permission/getPermissionIdsOfRole'),
-- RoleController
(21001, 'iam.role.add', '新增角色', 'POST', '/api/iam/role/add'),
(21002, 'iam.role.delete', '删除角色', 'GET', '/api/iam/role/delete'),
(21003, 'iam.role.edit', '修改角色', 'POST', '/api/iam/role/edit'),
(21004, 'iam.role.getPageConditionally', '条件查询角色分页', 'POST', '/api/iam/role/getPageConditionally'),
(21005, 'iam.role.getRoleIdsOfUser', '查询某用户所具有的角色ID列表', 'GET', '/api/iam/role/getRoleIdsOfUser'),
(21006, 'iam.role.grant', '为角色授权/解除授权', 'POST', '/api/iam/role/grant'),
