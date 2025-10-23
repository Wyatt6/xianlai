package fun.xianlai.app.iam.controller;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import fun.xianlai.app.iam.model.entity.rbac.User;
import fun.xianlai.app.iam.service.PermissionService;
import fun.xianlai.app.iam.service.RoleService;
import fun.xianlai.app.iam.service.UserService;
import fun.xianlai.core.annotation.ApiLog;
import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.feign.consumer.FeignCaptchaService;
import fun.xianlai.core.feign.consumer.FeignOptionService;
import fun.xianlai.core.response.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private FeignCaptchaService captchaService;
    @Autowired
    private FeignOptionService optionService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

//    @ApiLog("注册新用户")
    @PostMapping("/register")
    public RetResult register(@RequestBody User input) {
        String captchaKey = input.getCaptchaKey().trim();
        String captcha = input.getCaptcha().trim();
        String username = input.getUsername().trim();
        String password = input.getPassword().trim();
        log.info("请求参数: captchaKey=[{}], captcha=[{}], username=[{}]", captchaKey, captcha, username);

        captchaService.verifyCaptcha(captchaKey, captcha);
        if (!userService.checkUsernameFormat(username)) {
            throw new SysException("用户名格式错误");
        }
        if (!userService.checkPasswordFormat(password)) {
            throw new SysException("密码格式错误");
        }
        User newUser = userService.createUser(username, password);

        return new RetResult().success();
    }

    @ApiLog("用户登录")
    @PostMapping("/login")
    public RetResult login(@RequestBody User input) {
        String captchaKey = input.getCaptchaKey().trim();
        String captcha = input.getCaptcha().trim();
        String username = input.getUsername();
        String password = input.getPassword();
        log.info("请求参数: captchaKey=[{}], captcha=[{}], username=[{}]", captchaKey, captcha, username);

        captchaService.verifyCaptcha(captchaKey, captcha);
        if (!userService.checkUsernameFormat(username)) {
            throw new SysException("用户名格式错误");
        }
        if (!userService.checkPasswordFormat(password)) {
            throw new SysException("密码格式错误");
        }

        User user = userService.authentication(username, password);
        log.info("登录：Sa-Token框架自动生成token并缓存");
        StpUtil.login(user.getId(), new SaLoginParameter()
                .setTimeout(optionService.readValueInLong("token.timeout").orElse(43200L))
                .setActiveTimeout(optionService.readValueInLong("token.activeTimeout").orElse(3600L)));
        log.info("token=[{}]", StpUtil.getTokenValue());
        log.info("loginId=[{}]", StpUtil.getLoginIdAsLong());
        log.info("sessionId=[{}]", StpUtil.getSession().getId());
        SaSession session = StpUtil.getSession();

        log.info("User脱敏并缓存");
        user.setPassword(null);
        user.setSalt(null);
        session.set("user", user);

        log.info("获取角色和权限");
        List<String> roles = userService.getRoleList(user.getId());
        List<String> permissions = userService.getPermissionList(user.getId());

        return new RetResult().success()
                .addData("token", StpUtil.getTokenValue())
                .addData("tokenExpireTime", System.currentTimeMillis() + StpUtil.getTokenTimeout() * 1000)
                .addData("user", user)
                .addData("roles", roles)
                .addData("permissions", permissions);
    }

    @ApiLog("退出登录")
    @GetMapping("/logout")
    public RetResult logout() {
        try {
            log.info("token=[{}]", StpUtil.getTokenValue());
            log.info("loginId=[{}]", StpUtil.getLoginIdAsLong());
        } catch (Exception e) {
            if (e instanceof NotLoginException) {
                log.info("用户处于未登录状态");
            } else {
                throw e;
            }
        } finally {
            StpUtil.logout();
        }
        log.info("退出登录成功");
        return new RetResult().success();
    }
}
