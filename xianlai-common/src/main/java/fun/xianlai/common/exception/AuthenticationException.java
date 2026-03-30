package fun.xianlai.common.exception;

import fun.xianlai.common.response.RetCode;

/**
 * 认证异常
 *
 * @author WyattLau
 */
public class AuthenticationException extends BaseException {
    public AuthenticationException(String message) {
        super(RetCode.UNAUTHORIZED, message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(RetCode.UNAUTHORIZED, message, cause);
    }
}
