INSERT IGNORE INTO tb_common_sys_api(`id`, `call_path`, `description`, `request_method`, `url`) VALUE
(200001, 'iam.user.register', '注册新用户', 'POST', '/api/iam/user/register'),
(200002, 'iam.user.login', '用户登录', 'POST', '/api/common/user/login');

