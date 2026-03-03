package fun.xianlai.app.common.feign.producer;

import fun.xianlai.app.common.service.CaptchaService;
import fun.xianlai.core.response.RetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WyattLau
 */
@RestController
@RequestMapping("/feign/captcha")
public class FeignCaptchaController {
    @Autowired
    private CaptchaService captchaService;

    @GetMapping("/verifyCaptcha")
    public RetResult verifyCaptcha(@RequestParam String captchaKey, @RequestParam String captcha) {
        captchaService.verifyCaptcha(captchaKey, captcha);
        return new RetResult().writeFeignData(null);    // 即使服务是void类型的也要使用RetResult对象封装
    }
}
