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
import fun.xianlai.core.utils.StringUtils;
import fun.xianlai.core.utils.bean.BeanUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
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
