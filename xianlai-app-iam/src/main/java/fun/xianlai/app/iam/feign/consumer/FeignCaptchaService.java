package fun.xianlai.app.iam.feign.consumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author WyattLau
 */
@FeignClient(contextId = "xianlai-app-common-captcha", name = "xianlai-app-common", path = "/feign/captcha")
public interface FeignCaptchaService {
    @GetMapping("/verifyCaptcha")
    void verifyCaptcha(@RequestParam String captchaKey, @RequestParam String captcha);
}
