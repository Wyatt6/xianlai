package fun.xianlai.common.starter.handler;

import fun.xianlai.common.exception.BizException;
import fun.xianlai.common.exception.SysException;
import fun.xianlai.common.response.RetCode;
import fun.xianlai.common.response.RetResult;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Spring里多个异常处理器之间的处理顺序是按异常类的匹配程度来的
 * 匹配在注解@ExceptionHandler(XxxException.class)中声明的异常类
 * 最匹配的子类的方法就执行，否则就匹配该异常的父类有没有合适的handler
 * 所以这里最终都会交给@ExceptionHandler(Exception.class)的handler兜底执行
 *
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
        log.error("未知错误", e);
        return RetResult.fail(RetCode.SYS_ERROR, "服务器繁忙，请稍后再试");
    }

    @ExceptionHandler(SysException.class)
    public RetResult<?> handleSysException(SysException e) {
        log.error("系统错误：{}, {}", e.getCode(), e.getMessage(), e);
        return RetResult.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BizException.class)
    public RetResult<?> handleBizException(BizException e) {
        log.warn("业务错误：{}, {}", e.getCode(), e.getMessage());
        return RetResult.fail(e.getCode(), e.getMessage());
    }

    // Assert 参数校验异常
    @ExceptionHandler(IllegalArgumentException.class)
    public RetResult<?> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("参数校验失败: {}", e.getMessage());
        return RetResult.fail(RetCode.PARAM_ERROR, e.getMessage());
    }

    // @Validated 单参数校验异常
    @ExceptionHandler(ConstraintViolationException.class)
    public RetResult<?> handleConstraintViolation(ConstraintViolationException e) {
        String message = e.getConstraintViolations().iterator().next().getMessage();
        log.warn("参数校验失败: {}", message);
        return RetResult.fail(RetCode.PARAM_ERROR, message);
    }

    // @Valid DTO 校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RetResult<?> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = "未知原因";
        if (e.getBindingResult().hasFieldErrors()) {
            FieldError fieldError = e.getBindingResult().getFieldError();
            if (fieldError != null) {
                String defaultMessage = fieldError.getDefaultMessage();
                if (defaultMessage != null && !defaultMessage.isBlank()) {
                    message = defaultMessage;
                }
            }
        }
        log.warn("参数校验失败: {}", message);
        return RetResult.fail(RetCode.PARAM_ERROR, message);
    }
}
