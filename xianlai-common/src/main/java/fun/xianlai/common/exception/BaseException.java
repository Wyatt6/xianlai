package fun.xianlai.common.exception;

import lombok.Getter;

/**
 * @author WyattLau
 */
@Getter
public class BaseException extends RuntimeException{
    private final String code;

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
