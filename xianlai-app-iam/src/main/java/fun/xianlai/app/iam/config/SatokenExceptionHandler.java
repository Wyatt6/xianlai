package fun.xianlai.app.iam.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.basic.support.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author WyattLau
 */
@Slf4j
@ControllerAdvice
public class SatokenExceptionHandler {
    @ExceptionHandler(NotLoginException.class)
    @ResponseBody
    public RetResult notLoginExceptionHandler(Exception e) {
        log.info("Sa-Token验证异常：未登录");
        RetResult result = new RetResult();
        result.fail().setFailCode("401").setFailMessage("用户未登录").setTraceId(MDC.get("traceId"));
        log.info("响应: {}", JSONObject.toJSONString(result));
        return result;
    }

    @ExceptionHandler(NotRoleException.class)
    @ResponseBody
    public RetResult notRoleExceptionHandler(Exception e) {
        log.info("Sa-Token验证异常：角色异常");
        RetResult result = new RetResult();
        result.fail().setFailCode("403").setFailMessage("用户权限不足").setTraceId(MDC.get("traceId"));
        log.info("响应: {}", JSONObject.toJSONString(result));
        return result;
    }

    @ExceptionHandler(NotPermissionException.class)
    @ResponseBody
    public RetResult notPermissionExceptionHandler(Exception e) {
        log.info("Sa-Token验证异常：权限异常");
        RetResult result = new RetResult();
        result.fail().setFailCode("403").setFailMessage("用户权限不足").setTraceId(MDC.get("traceId"));
        log.info("响应: {}", JSONObject.toJSONString(result));
        return result;
    }
}
