package fun.xianlai.common.exception;

import fun.xianlai.common.response.RetCode;

/**
 * 认证异常
 *
 * @author WyattLau
 */
public class AuthenticationExeption extends BaseException {
    public AuthenticationExeption(String message) {
        super(RetCode.UNAUTHORIZED, message);
    }

    public AuthenticationExeption(String message, Throwable cause) {
        super(RetCode.UNAUTHORIZED, message, cause);
    }
}
