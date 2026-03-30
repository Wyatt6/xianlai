package fun.xianlai.common.starter.handler;

import fun.xianlai.common.exception.BizException;
import fun.xianlai.common.exception.SysException;
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
public class GlobalExceptionHandler {
    /**
     * 最终兜底的异常处理
     */
    @ExceptionHandler(Exception.class)
    public RetResult<?> handleException(Exception e) {
        log.error("未知错误：", e);
        return RetResult.fail(RetCode.SYS_ERROR, "服务器繁忙，请稍后再试");
    }

    @ExceptionHandler(BizException.class)
    public RetResult<?> handleBizException(BizException e) {
        log.error("业务错误：{}", e.getMessage());
        return RetResult.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(SysException.class)
    public RetResult<?> handleSysException(SysException e) {
        log.error("系统错误：", e);
        return RetResult.fail(e.getCode(), e.getMessage());
    }
}
