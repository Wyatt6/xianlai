INSERT IGNORE INTO tb_common_sys_path(`id`, `sort_id`, `name`, `path`) VALUE
-- 门户 --
(10001, 10001, 'PORTAL', '/portal'),
(10002, 10002, 'LOGIN', '/portal/login'),
(10003, 10003, 'REGISTER', '/portal/register'),
(10004, 10004, 'RESET_PASSWORD', '/portal/reset-password'),
-- 首页 --
(11001, 11001, 'INDEX', '/'),
(11002, 11002, 'INDEX_REDIRECT', '/homepage'),
(11003, 11003, 'HOMEPAGE', '/homepage'),
-- 错误页面 --
(12001, 12001, 'NOT_AUTHORIZED_EMBEDDED', '/403/embedded'),
(12002, 12002, 'NOT_AUTHORIZED', '/403'),
(12003, 12003, 'NOT_FOUND', '/404'),
(12004, 12004, 'NOT_CONNECTED', '/500'),
-- 最终无匹配 --
(99999, 99999, 'FINAL_NOT_MATCH', '/:catchAll(.*)');

INSERT IGNORE INTO tb_common_sys_menu(
    `id`, `sort_id`, `parent_id`, `icon`, `title`,
    `path_name`, `need_permission`, `permission`, `active`
) VALUE
(10001, 10001, 0, 'ri-home-4-fill', '首页', 'HOMEPAGE', 0, null, 1);

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
(9000001, 9000001, 'not_authorized_embedded', 'NOT_AUTHORIZED_EMBEDDED', null, 'errors/403.vue', 1, 0, null, 0, null, 100005),
(9000002, 9000002, 'not_authorized', 'NOT_AUTHORIZED', null, 'errors/403.vue', 1, 0, null, 0, null, 0),
(9000003, 9000003, 'not_found', 'NOT_FOUND', null, 'errors/404.vue', 0, 0, null, 0, null, 0),
(9000004, 9000004, 'not_connected', 'NOT_CONNECTED', null, 'errors/500.vue', 0, 0, null, 0, null, 0),
-- 最终无匹配 --
(9999999, 9999999, 'final_not_match', 'FINAL_NOT_MATCH', 'NOT_FOUND', null, 0, 0, null, 0, null, 0);
