INSERT IGNORE INTO tb_common_sys_path(`name`, `path`, `sort_id`) VALUE
-- 门户 --
('PORTAL', '/portal', 100001),
('LOGIN', '/portal/login', 100002),
('REGISTER', '/portal/register', 100003),
('RESET_PASSWORD', '/portal/reset-password', 100004),
-- 首页 --
('INDEX', '/', 100005),
('INDEX_REDIRECT', '/homepage', 100006),
('HOMEPAGE', '/homepage', 100007),
-- 错误页面 --
('NOT_AUTHORIZED_EMBEDDED', '/401/embedded', 9000001),
('NOT_AUTHORIZED', '/401', 9000002),
('NOT_FOUND', '/404', 9000003),
('NOT_CONNECTED', '/500', 9000004),
-- 最终无匹配 --
('FINAL_NOT_MATCH', '/:catchAll(.*)', 9999999);

INSERT IGNORE INTO tb_common_sys_menu(`id`, `sort_id`, `icon`, `title`, `path_name`, `need_permission`, `permission`, `active`, `parent_id`) VALUE
(100001, 100001, 'ri-home-4-fill', '首页', 'HOMEPAGE', 0, null, 1, 0);

INSERT IGNORE INTO tb_common_sys_route(`id`, `sort_id`, `name`, `path_name`, `redirect_path_name`, `component_path`, `need_login`, `need_permission`, `permission`, `show_tag`, `tag_title`, `parent_id`) VALUE
-- 门户 --
(100001, 100001, 'portal', 'PORTAL', 'LOGIN', 'portal/index.vue', 0, 0, null, 0, null, 0),
(100002, 100002, 'register', 'REGISTER', null, 'portal/register/index.vue', 0, 0, null, 0, null, 100001),
(100003, 100003, 'login', 'LOGIN', null, 'portal/login/index.vue', 0, 0, null, 0, null, 100001),
(100004, 100004, 'reset_password', 'RESET_PASSWORD', null, 'portal/reset_password/index.vue', 0, 0, null, 0, null, 100001),
-- 首页 --
(100005, 100005, 'layout', 'INDEX', 'INDEX_REDIRECT', 'layout/index.vue', 1, 0, null, 0, null, 0),
(100006, 100006, 'homepage', 'HOMEPAGE', null, 'homepage/index.vue', 1, 0, null, 0, null, 100005),
-- 错误页面 --
(9000001, 9000001, 'not_authorized_embedded', 'NOT_AUTHORIZED_EMBEDDED', null, 'errors/401.vue', 1, 0, null, 0, null, 100005),
(9000002, 9000002, 'not_authorized', 'NOT_AUTHORIZED', null, 'errors/401.vue', 1, 0, null, 0, null, 0),
(9000003, 9000003, 'not_found', 'NOT_FOUND', null, 'errors/404.vue', 0, 0, null, 0, null, 0),
(9000004, 9000004, 'not_connected', 'NOT_CONNECTED', null, 'errors/500.vue', 0, 0, null, 0, null, 0),
-- 最终无匹配 --
(9999999, 9999999, 'final_not_match', 'FINAL_NOT_MATCH', 'NOT_FOUND', null, 0, 0, null, 0, null, 0);
