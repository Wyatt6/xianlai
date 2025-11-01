INSERT IGNORE INTO tb_common_sys_path(`id`, `sort_id`, `name`, `path`) VALUE
-- ------- --
-- 公共路径 --
-- ------- --
-- 门户
(10001, 10001, 'PORTAL', '/portal'),
(10002, 10002, 'LOGIN', '/portal/login'),
(10003, 10003, 'REGISTER', '/portal/register'),
(10004, 10004, 'RESET_PASSWORD', '/portal/reset-password'),
-- 首页
(11001, 11001, 'INDEX', '/'),
(11002, 11002, 'INDEX_REDIRECT', '/homepage'),
(11003, 11003, 'HOMEPAGE', '/homepage'),
-- 系统设置
(12001, 12001, 'SETTING', '/setting'),
(12002, 12002, 'SETTING_REDIRECT', '/setting/path'),
(12003, 12003, 'SETTING_PATH', '/setting/path'),
(12004, 12004, 'SETTING_MENU', '/setting/menu'),
(12005, 12005, 'SETTING_ROUTE', '/setting/route'),
(12006, 12006, 'SETTING_OPTION', '/setting/option'),
(12007, 12007, 'SETTING_API', '/setting/api'),
-- 错误页面
(99001, 99001, 'NOT_AUTHORIZED_EMBEDDED', '/403/embedded'),
(99002, 99002, 'NOT_AUTHORIZED', '/403'),
(99003, 99003, 'NOT_FOUND', '/404'),
(99004, 99004, 'NOT_CONNECTED', '/500'),
-- 最终无匹配
(99999, 99999, 'FINAL_NOT_MATCH', '/:catchAll(.*)'),

-- ---------- --
-- IAM模块路径 --
-- ---------- --
(20001, 20001, 'IAM', '/iam'),
(20002, 20002, 'IAM_REDIRECT', '/iam/user-manage'),
(20003, 20003, 'IAM_USER_MANAGE', '/iam/user-manage'),
(20004, 20004, 'IAM_ROLE_MANAGE', '/iam/role-manage'),
(20005, 20005, 'IAM_PERMISSION_MANAGE', '/iam/permission-manage');
