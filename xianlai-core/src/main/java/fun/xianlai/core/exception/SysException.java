package fun.xianlai.core.exception;

/**
 * @author WyattLau
 */
public class SysException extends RuntimeException {
    public SysException() {
        super("系统错误");
    }

    public SysException(String message) {
        super(message);
    }

    public SysException(String message, Throwable cause) {
        super(message, cause);
    }
}
