INSERT IGNORE INTO tb_common_sys_api(`id`, `call_path`, `description`, `request_method`, `url`) VALUE
(10001, 'common.init.getInitData', '获取初始化数据', 'GET', '/api/common/init/getInitData'),
(10002, 'common.captcha.getCaptcha', '获取验证码', 'GET', '/api/common/captcha/getCaptcha');
