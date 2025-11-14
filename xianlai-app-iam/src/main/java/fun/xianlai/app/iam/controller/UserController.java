package fun.xianlai.app.iam.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import fun.xianlai.app.iam.model.entity.other.Profile;
import fun.xianlai.app.iam.model.entity.other.UserInfo;
import fun.xianlai.app.iam.model.entity.rbac.User;
import fun.xianlai.app.iam.model.form.BindForm;
import fun.xianlai.app.iam.model.form.ChangePasswordForm;
import fun.xianlai.app.iam.model.form.UserCondition;
import fun.xianlai.app.iam.service.PermissionService;
import fun.xianlai.app.iam.service.RoleService;
import fun.xianlai.app.iam.service.UserService;
import fun.xianlai.core.annotation.ApiLog;
import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.feign.consumer.FeignCaptchaService;
import fun.xianlai.core.feign.consumer.FeignOptionService;
import fun.xianlai.core.response.RetResult;
import fun.xianlai.core.utils.EntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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

    @ApiLog("创建新用户")
    @SaCheckLogin
    @SaCheckPermission("user:add")
    @PostMapping("/createUser")
    public RetResult createUser(@RequestBody User form) {
        EntityUtil.trimString(form);
        log.info("请求参数: username=[{}], active=[{}]", form.getUsername(), form.getActive());
        if (!userService.matchUsernameFormat(form.getUsername())) {
            throw new SysException("用户名格式错误");
        }
        if (!userService.matchPasswordFormat(form.getPassword())) {
            throw new SysException("密码格式错误");
        }
        return new RetResult().success().setData(userService.createUser(form.getUsername(), form.getPassword(), form.getActive()));
    }

    @ApiLog("注册新用户")
    @PostMapping("/register")
    public RetResult register(@RequestBody User form) {
        EntityUtil.trimString(form);
        String captchaKey = form.getCaptchaKey();
        String captcha = form.getCaptcha();
        String username = form.getUsername();
        String password = form.getPassword();
        log.info("请求参数: captchaKey=[{}], captcha=[{}], username=[{}]", captchaKey, captcha, username);
        captchaService.verifyCaptcha(captchaKey, captcha);
        if (!userService.matchUsernameFormat(form.getUsername())) {
            throw new SysException("用户名格式错误");
        }
        if (!userService.matchPasswordFormat(form.getPassword())) {
            throw new SysException("密码格式错误");
        }
        return new RetResult().success().setData(userService.createUser(username, password, true));
    }

    @ApiLog("用户登录")
    @PostMapping("/login")
    public RetResult login(@RequestBody User form) {
        EntityUtil.trimString(form);
        String captchaKey = form.getCaptchaKey();
        String captcha = form.getCaptcha();
        String username = form.getUsername();
        String password = form.getPassword();
        log.info("请求参数: captchaKey=[{}], captcha=[{}], username=[{}]", captchaKey, captcha, username);

        captchaService.verifyCaptcha(captchaKey, captcha);
        if (!userService.matchUsernameFormat(username)) {
            throw new SysException("用户名格式错误");
        }
        if (!userService.matchPasswordFormat(password)) {
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

        log.info("获取角色和权限并缓存");
        // 下方Service已包含缓存
        List<String> roles = userService.getRoleList(user.getId());
        List<String> permissions = userService.getPermissionList(user.getId());

        log.info("获取Profile并缓存");
        Profile profile = userService.exportProfile(user.getId());
        session.set("profile", profile);

        return new RetResult().success()
                .addData("token", StpUtil.getTokenValue())
                .addData("tokenExpireTime", System.currentTimeMillis() + StpUtil.getTokenTimeout() * 1000)
                .addData("user", user)
                .addData("roles", roles)
                .addData("permissions", permissions)
                .addData("profile", profile);
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

    @ApiLog("修改密码")
    @SaCheckLogin
    @PostMapping("/changePassword")
    public RetResult changePassword(@RequestBody ChangePasswordForm form) {
        EntityUtil.trimString(form);
        log.info("请求参数: {}", form);
        if (!userService.matchPasswordFormat(form.getOldPassword())) {
            throw new SysException("旧密码格式错误");
        }
        if (!userService.matchPasswordFormat(form.getNewPassword())) {
            throw new SysException("新密码格式错误");
        }
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("userId=[{}]", userId);
        try {
            userService.authentication(userId, form.getOldPassword());
        } catch (SysException e) {
            throw new SysException("当前密码错误");
        }
        userService.changePassword(userId, form.getNewPassword());
        return new RetResult().success();
    }

    /**
     * 查询条件为空时查询全量数据
     * 页码<0或页大小<=0时不分页
     *
     * @param pageNum   页码
     * @param pageSize  页大小
     * @param condition 查询条件
     * @return {pageNum 页码, pageSize 页大小, totalPages 页码总数, totalElements 总条数, content 分页数据}
     */
    @ApiLog("条件查询用户分页")
    @SaCheckLogin
    @SaCheckPermission("user:query")
    @PostMapping("/getPageConditionally")
    public RetResult getPageConditionally(@RequestParam("pageNum") int pageNum,
                                          @RequestParam("pageSize") int pageSize,
                                          @RequestBody(required = false) UserCondition condition) {
        log.info("请求参数：pageNum=[{}], pageSize=[{}], condition=[{}]", pageNum, pageSize, condition);
        Page<UserInfo> userInfoList = userService.getUsersByPageConditionally(pageNum, pageSize, condition);
        return new RetResult().success()
                .addData("pageNum", pageNum)
                .addData("pageSize", pageSize)
                .addData("totalPages", userInfoList.getTotalPages())
                .addData("totalElements", userInfoList.getTotalElements())
                .addData("content", userInfoList.getContent());
    }

    @ApiLog("为用户绑定/解除绑定角色")
    @SaCheckLogin
    @SaCheckPermission("user:bind")
    @PostMapping("/bind")
    public RetResult bind(@RequestBody BindForm form) {
        log.info("请求参数: {}", form);
        Optional<User> user = userService.findByUserId(form.getUserId());
        if (user.isEmpty()) {
            throw new SysException("用户已注销");
        }
        List<Long> failBind = null;
        List<Long> failCancel = null;
        try {
            log.info("绑定");
            failBind = userService.bind(form.getUserId(), form.getBind());
        } catch (IllegalArgumentException e) {
            log.info("无须绑定");
        }
        try {
            log.info("解除绑定");
            failCancel = userService.cancelBind(form.getUserId(), form.getCancel());
        } catch (IllegalArgumentException e) {
            log.info("无须解除绑定");
        }
        return new RetResult().success()
                .addData("failBind", failBind)
                .addData("failCancel", failCancel);
    }

    @ApiLog("修改用户信息/注销用户")
    @SaCheckLogin
    @PostMapping("/editUserInfo")
    public RetResult editUserInfo(@RequestBody UserInfo form) {
        log.info("请求参数: {}", form);
        return new RetResult().success().setData(userService.editUserInfo(form));
    }
}
