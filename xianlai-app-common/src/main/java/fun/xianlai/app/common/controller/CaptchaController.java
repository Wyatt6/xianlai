package fun.xianlai.app.common.controller;

import fun.xianlai.app.common.service.CaptchaService;
import fun.xianlai.core.annotation.ApiLog;
import fun.xianlai.core.response.RetResult;
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

    /**
     * { captchaKey: 验证码KEY, captchaImage: 验证码Base64图像 }
     */
    @ApiLog("获取验证码")
    @GetMapping("/getCaptcha")
    public RetResult getCaptcha() {
        return new RetResult().success().setData(captchaService.generateCaptcha());
    }
}
