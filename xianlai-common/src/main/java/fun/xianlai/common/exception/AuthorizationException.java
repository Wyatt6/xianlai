package fun.xianlai.common.exception;

import fun.xianlai.common.response.RetCode;

/**
 * 授权异常
 *
 * @author WyattLau
 */
public class AuthorizationException extends BaseException {
    public AuthorizationException(String message) {
        super(RetCode.FORBIDDEN, message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(RetCode.FORBIDDEN, message, cause);
    }
}
