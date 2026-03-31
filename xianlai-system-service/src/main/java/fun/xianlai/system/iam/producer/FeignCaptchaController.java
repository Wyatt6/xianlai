package fun.xianlai.system.iam.producer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WyattLau
 */
@RestController
@RequestMapping("/feign/captcha")
public class FeignCaptchaController {
//    @Autowired
//    private CaptchaService captchaService;
//
//    @GetMapping("/verifyCaptcha")
//    public RetResult verifyCaptcha(@RequestParam String captchaKey, @RequestParam String captcha) {
//        captchaService.verifyCaptcha(captchaKey, captcha);
//        return new RetResult().writeFeignData(null);    // 即使服务是void类型的也要使用RetResult对象封装
//    }
}
