package fun.xianlai.system.core.controller;

import fun.xianlai.common.annotation.ApiLog;
import fun.xianlai.common.response.RetResult;
import fun.xianlai.system.core.service.CaptchaService;
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
@RequestMapping("/core/captcha")
public class CaptchaController {
    @Autowired
    private CaptchaService captchaService;

    /**
     * @return { captchaKey: 验证码KEY, captchaImage: 验证码Base64图像 }
     */
    @ApiLog("获取验证码")
    @GetMapping("/getCaptcha")
    public RetResult<?> getCaptcha() {
        return RetResult.success(captchaService.generateCaptcha());
    }
}
