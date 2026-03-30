package fun.xianlai.common.starter.handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.exception.NotSafeException;
import cn.dev33.satoken.exception.SaTokenContextException;
import cn.dev33.satoken.exception.SaTokenException;
import fun.xianlai.common.response.RetCode;
import fun.xianlai.common.response.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author WyattLau
 */
@Slf4j
@RestControllerAdvice
public class SatokenExceptionHandler {
    /**
     * SaToken兜底异常处理
     */
    @ExceptionHandler(SaTokenException.class)
    public RetResult<?> handleSaTokenException(SaTokenException e) {
        log.info("Sa-Token 未知异常: {}", e.getMessage());
        return RetResult.fail(RetCode.SYS_ERROR, "系统认证/鉴权异常，请联系管理员");
    }

    @ExceptionHandler(SaTokenContextException.class)
    public RetResult<?> handleSaTokenContextException(SaTokenContextException e) {
        log.error("Sa-Token 上下文异常，无法获取会话信息：{}", e.getMessage());
        return RetResult.fail(RetCode.SYS_ERROR, "系统环境异常，请稍后重试");
    }

    @ExceptionHandler(NotLoginException.class)
    public RetResult<?> handleNotLoginException(NotLoginException e) {
        log.info("Sa-Token 认证异常: {}", e.getMessage());
        String message = switch (e.getType()) {
            case NotLoginException.NOT_TOKEN -> "请登录后继续操作";
            case NotLoginException.INVALID_TOKEN -> "登录状态无效，请重新登录";
            case NotLoginException.TOKEN_TIMEOUT -> "登录已过期，请重新登录";
            case NotLoginException.BE_REPLACED -> "您的账号已在其他设备登录，请重新登录";
            case NotLoginException.KICK_OUT -> "您的账号已被强制下线，请重新登录";
            case NotLoginException.TOKEN_FREEZE -> "您的账号已被冻结，请联系管理员";
            case NotLoginException.NO_PREFIX -> "登录凭证格式错误，请重新登录";
            default -> "未登录，请登录后继续操作";
        };
        log.info(message);
        return RetResult.fail(RetCode.UNAUTHORIZED, message);
    }

    @ExceptionHandler(NotSafeException.class)
    public RetResult<?> handleNotSafeException(NotSafeException e) {
        log.error("Sa-Token 安全验证异常：{}", e.getMessage());
        return RetResult.fail(RetCode.FORBIDDEN, "安全验证未通过，请完成安全验证后继续");
    }

    @ExceptionHandler(NotRoleException.class)
    public RetResult<?> handleNotRoleException(NotRoleException e) {
        log.error("Sa-Token 角色异常：无[{}]角色", e.getRole());
        return RetResult.fail(RetCode.FORBIDDEN, "权限不足，无法访问该资源");
    }

    @ExceptionHandler(NotPermissionException.class)
    public RetResult<?> handleNotPermissionException(NotPermissionException e) {
        log.info("Sa-Token 权限异常：无[{}]权限", e.getPermission());
        return RetResult.fail(RetCode.FORBIDDEN, "权限不足，无法访问该资源");
    }
}
