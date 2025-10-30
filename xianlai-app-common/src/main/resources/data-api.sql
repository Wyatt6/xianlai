INSERT IGNORE INTO tb_common_sys_api(`id`, `call_path`, `description`, `request_method`, `url`) VALUE
-- init
(10001, 'common.init.getInitData', '获取初始化数据', 'GET', '/api/common/init/getInitData'),
-- capcha
(11001, 'common.captcha.getCaptcha', '获取验证码', 'GET', '/api/common/captcha/getCaptcha'),
-- api
(12001, 'common.api.getPageConditionally', '条件查询接口分页', 'POST', '/api/common/api/getPageConditionally');
