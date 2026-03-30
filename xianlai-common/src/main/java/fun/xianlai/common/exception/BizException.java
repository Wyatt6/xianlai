package fun.xianlai.common.exception;

import fun.xianlai.common.response.RetCode;

/**
 * @author WyattLau
 */
public class BizException extends BaseException {
    public BizException(String code, String message) {
        super(code, message);
    }

    public BizException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public BizException(String message) {
        super(RetCode.BIZ_ERROR, message);
    }

    public BizException(String message, Throwable cause) {
        super(RetCode.BIZ_ERROR, message, cause);
    }

    public static BizException paramError(String message) {
        return new BizException(RetCode.PARAM_ERROR, message);
    }

    public static BizException paramError(String message, Throwable cause) {
        return new BizException(RetCode.PARAM_ERROR, message, cause);
    }

    public static BizException dataNotFound(String message) {
        return new BizException(RetCode.DATA_NOT_FOUND, message);
    }

    public static BizException dataNotFound(String message, Throwable cause) {
        return new BizException(RetCode.DATA_NOT_FOUND, message, cause);
    }

    public static BizException dataExists(String message) {
        return new BizException(RetCode.DATA_ALREADY_EXISTS, message);
    }

    public static BizException dataExists(String message, Throwable cause) {
        return new BizException(RetCode.DATA_ALREADY_EXISTS, message, cause);
    }
}
