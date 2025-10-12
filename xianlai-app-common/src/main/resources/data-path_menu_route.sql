INSERT IGNORE INTO tb_common_sys_path(`name`, `path`, `sort_id`) VALUE
-- 门户 --
('PORTAL', '/portal', 100001),
('LOGIN', '/portal/login', 100002),
('REGISTER', '/portal/register', 100003),
('RESET_PASSWORD', '/portal/reset-password', 100004),
-- 首页 --
('INDEX', '/', 100005),
('HOMEPAGE', '/homepage', 100006);


INSERT IGNORE INTO tb_common_sys_menu(`id`, `sort_id`, `icon`, `title`, `path_name`, `permission`, `active`, `parent_id`) VALUE
(100001, 100001, 'ri-home-4-fill', '首页', 'HOMEPAGE', null, 1, 0);

INSERT IGNORE INTO tb_common_sys_route(`id`, `sort_id`, `name`, `path_name`, `redirect_path_name`, `component_path`, `need_login`, `need_permission`, `permission`, `show_tag`, `tag_title`, `parent_id`) VALUE
-- 门户 --
(100001, 100001, 'portal', 'PORTAL', 'LOGIN', 'portal/index.vue', 0, 0, null, 0, null, 0),
(100002, 100002, 'register', 'REGISTER', null, 'portal/register/index.vue', 0, 0, null, 0, null, 100001),
(100003, 100003, 'login', 'LOGIN', null, 'portal/login/index.vue', 0, 0, null, 0, null, 100001),
(100004, 100004, 'reset_password', 'RESET_PASSWORD', null, 'portal/reset_password/index.vue', 0, 0, null, 0, null, 100001),
-- 首页 --
(100005, 100005, 'layout', 'INDEX', 'HOMEPAGE', 'layout/index.vue', 1, 0, null, 0, null, 0),
(100006, 100006, 'homepage', 'HOMEPAGE', null, 'homepage/index.vue', 1, 0, null, 0, null, 100005);
