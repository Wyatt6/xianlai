package fun.xianlai.app.iam.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import fun.xianlai.app.iam.model.entity.other.Department;
import fun.xianlai.app.iam.model.entity.other.Position;
import fun.xianlai.app.iam.model.entity.other.Profile;
import fun.xianlai.app.iam.model.entity.rbac.User;
import fun.xianlai.app.iam.service.DepartmentService;
import fun.xianlai.app.iam.service.PositionService;
import fun.xianlai.app.iam.service.ProfileService;
import fun.xianlai.app.iam.service.UserService;
import fun.xianlai.app.iam.feign.consumer.FeignCaptchaService;
import fun.xianlai.app.iam.feign.consumer.FeignOptionService;
import fun.xianlai.basic.annotation.ControllerLog;
import fun.xianlai.basic.exception.SysException;
import fun.xianlai.basic.support.RetResult;
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
    private FeignCaptchaService captchaService;
    @Autowired
    private FeignOptionService optionService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private PositionService positionService;


    @ControllerLog("注册新用户")
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
        profileService.createProfile(newUser.getId());

        return new RetResult().success();
    }

    @ControllerLog("用户登录")
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
        log.info("loginId=[{}]", StpUtil.getLoginId());
        log.info("token=[{}]", StpUtil.getTokenValue());
        log.info("sessionId=[{}]", StpUtil.getSession().getId());
        SaSession session = StpUtil.getSession();

        // TODO 登录日志

        log.info("User脱敏并缓存");
        user.setPassword(null);
        user.setSalt(null);
        session.set("user", user);

        log.info("获取Profile并缓存");
        Profile profile = profileService.getProfile(user.getId());
        session.set("profile", profile);

        Department department = null;
        Position position = null;
        if (profile != null) {
            if (profile.getMainDepartmentId() != null) {
                log.info("获取Department并缓存");
                department = departmentService.getDepartment(profile.getMainDepartmentId());
                session.set("department", department);
            }
            if (profile.getMainPositionId() != null) {
                log.info("获取Position并缓存");
                position = positionService.getPosition(profile.getMainPositionId());
                session.set("position", position);
            }
        }

        // TODO 登录日志
        // TODO 缓存用户数据

        return new RetResult().success()
                .addData("token", StpUtil.getTokenValue())
                .addData("tokenExpireTime", System.currentTimeMillis() + StpUtil.getTokenTimeout() * 1000)
                .addData("user", user)
                .addData("profile", profile)
                .addData("department", department)
                .addData("position", position);
    }

    @ControllerLog("退出登录")
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

        // TODO 清除缓存

        log.info("退出登录成功");
        return new RetResult().success();
    }
}
