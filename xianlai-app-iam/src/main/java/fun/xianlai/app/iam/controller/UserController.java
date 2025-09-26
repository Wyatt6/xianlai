package fun.xianlai.app.iam.controller;

import fun.xianlai.basic.annotation.ControllerLog;
import fun.xianlai.basic.exception.SysException;
import fun.xianlai.basic.support.RetResult;
import fun.xianlai.app.iam.model.entity.rbac.User;
import fun.xianlai.app.iam.service.UserService;
import fun.xianlai.app.iam.service.feign.FeignOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FeignOptionService feignOptionService;

    @GetMapping("/test")
    public void test() {
        System.out.println(feignOptionService.getCertainBackLoadSysOptionValueFromCache("captcha.length"));
    }

    @ControllerLog("注册新用户")
    @PostMapping("/register")
    public RetResult register(@RequestBody User input) {
        String captchaKey = input.getCaptchaKey().trim();
        String captcha = input.getCaptcha().trim();
        String username = input.getUsername().trim();
        String password = input.getPassword().trim();
        log.info("请求参数: captchaKey=[{}], captcha=[{}], username=[{}]", captchaKey, captcha, username);

//        captchaService.verifyCaptcha(captchaKey, captcha);
        if (!userService.checkUsernameFormat(username)) {
            throw new SysException("用户名格式错误");
        }
        if (!userService.checkPasswordFormat(password)) {
            throw new SysException("密码格式错误");
        }
//        User user = userService.createUser(username, password);

        return new RetResult().success();
    }
}
