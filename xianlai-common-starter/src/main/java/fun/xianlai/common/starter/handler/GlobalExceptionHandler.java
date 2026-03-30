package fun.xianlai.common.starter.handler;

import fun.xianlai.common.exception.BizException;
import fun.xianlai.common.exception.SysException;

/**
 * @author WyattLau
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BizException.class)
    public RetResult<?> handleBizException(BizException e) {
        log.error("业务异常：{}", e.getMessage());
        return RetResult.fail(e.getCode(), e.getMessage())
                .setTraceId(getTraceId());
    }

    @ExceptionHandler(SysException.class)
    public RetResult<?> handleSysException(SysException e) {
        log.error("系统异常：", e);
        return RetResult.fail(e.getCode(), e.getMessage())
                .setTraceId(getTraceId());
    }

    @ExceptionHandler(Exception.class)
    public RetResult<?> handleException(Exception e) {
        log.error("服务器异常：", e);
        return RetResult.fail("500", "服务器繁忙，请稍后再试")
                .setTraceId(getTraceId());
    }

    private String getTraceId() {
        try {
            RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
            return (String) attrs.getAttribute(TraceIdInterceptor.TRACE_ID, RequestAttributes.SCOPE_REQUEST);
        } catch (Exception e) {
            return null;
        }
    }
}
