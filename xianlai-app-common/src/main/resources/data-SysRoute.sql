INSERT IGNORE INTO tb_common_sys_route(`id`, `sort_id`, `name`, `path_name`, `redirect_path_name`, `component_path`, `need_login`, `need_permission`, `permission`, `parent_id`) VALUE
(100001, 100001, 'portal', 'PORTAL', 'LOGIN', 'portal/index.vue', 0, 0, null, 0),
(100002, 100002, 'register', 'REGISTER', null, 'portal/register/index.vue', 0, 0, null, 100001),
(100003, 100003, 'login', 'LOGIN', null, 'portal/login/index.vue', 0, 0, null, 100001),
(100004, 100004, 'reset_password', 'RESET_PASSWORD', null, 'portal/reset_password/index.vue', 0, 0, null, 100001);
