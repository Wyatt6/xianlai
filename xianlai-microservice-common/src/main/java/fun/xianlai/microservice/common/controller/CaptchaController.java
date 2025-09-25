package fun.xianlai.microservice.common.controller;

import fun.xianlai.basic.annotation.ControllerLog;
import fun.xianlai.basic.support.RetResult;
import fun.xianlai.microservice.common.service.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/captcha")
public class CaptchaController {
    @Autowired
    private CaptchaService captchaService;

    @ControllerLog("获取验证码")
    @GetMapping("/getCaptcha")
    public RetResult getCaptcha() {
        // {captchaKey 验证码KEY, captchaImage 验证码Base64图像}
        return new RetResult().success().setData(captchaService.generateCaptcha());
    }
}
