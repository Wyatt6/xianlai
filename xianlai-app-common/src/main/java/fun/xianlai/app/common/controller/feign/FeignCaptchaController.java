package fun.xianlai.app.common.controller.feign;

import fun.xianlai.app.common.service.CaptchaService;
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
    public void verifyCaptcha(@RequestParam String captchaKey, @RequestParam String captcha) {
        captchaService.verifyCaptcha(captchaKey, captcha);
    }
}
