package fun.xianlai.common.exception;

import fun.xianlai.common.response.RetCode;

/**
 * @author WyattLau
 */
public class BizException extends BaseException {
    public BizException(String code, String message) {
        super(code, message);
    }

    public BizException(String message) {
        super(RetCode.BIZ_ERROR, message);
    }

    public static BizException paramError(String message) {
        return new BizException(RetCode.PARAM_ERROR, message);
    }

    public static BizException dataNotFound(String message) {
        return new BizException(RetCode.DATA_NOT_FOUND, message);
    }

    public static BizException dataExists(String message) {
        return new BizException(RetCode.DATA_ALREADY_EXISTS, message);
    }
}
