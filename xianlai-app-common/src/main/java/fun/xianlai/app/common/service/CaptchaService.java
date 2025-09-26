package fun.xianlai.app.common.service;


import fun.xianlai.basic.support.DataMap;

/**
 * @author WyattLau
 */
public interface CaptchaService {
    /**
     * 生成验证码
     *
     * @return {captchaKey 验证码KEY, captchaImage 验证码Base64图像}
     */
    DataMap generateCaptcha();

    /**
     * 校验验证码
     * 若校验错误则抛出异常
     *
     * @param captchaKey 验证码KEY
     * @param captcha    输入的验证码
     */
    void verifyCaptcha(String captchaKey, String captcha);
}
