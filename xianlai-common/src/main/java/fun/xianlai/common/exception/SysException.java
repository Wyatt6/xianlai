package fun.xianlai.common.exception;

import fun.xianlai.common.response.RetCode;

/**
 * @author WyattLau
 */
public class SysException extends BaseException {
    public SysException(String code, String message) {
        super(code, message);
    }

    public SysException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public SysException(String message) {
        super(RetCode.SYS_ERROR, message);
    }

    public SysException(String message, Throwable cause) {
        super(RetCode.SYS_ERROR, message, cause);
    }

    public static SysException dbError(String message) {
        return new SysException(RetCode.DB_ERROR, message);
    }

    public static SysException dbError(String message, Throwable cause) {
        return new SysException(RetCode.DB_ERROR, message, cause);
    }

    public static SysException cacheError(String message) {
        return new SysException(RetCode.CACHE_ERROR, message);
    }

    public static SysException cacheError(String message, Throwable cause) {
        return new SysException(RetCode.CACHE_ERROR, message, cause);
    }

    public static SysException thirdPartyError(String message) {
        return new SysException(RetCode.THIRD_SERVICE_ERROR, message);
    }

    public static SysException thirdPartyError(String message, Throwable cause) {
        return new SysException(RetCode.THIRD_SERVICE_ERROR, message, cause);
    }
}
