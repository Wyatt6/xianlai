INSERT IGNORE INTO tb_common_sys_api(`id`, `call_path`, `description`, `request_method`, `url`) VALUE
(100001, 'common.init.getInitData', '获取初始化数据', 'GET', '/api/common/init/getInitData'),
(100002, 'common.captcha.getCaptcha', '获取验证码', 'GET', '/api/common/captcha/getCaptcha');

