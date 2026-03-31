package fun.xianlai.common.context;

/**
 * @author WyattLau
 */
public final class RequestContext {
    private static final ThreadLocal<Long> ST_TIMESTAMP = new ThreadLocal<>();

    public static void setStTimestamp(Long stTimestamp) {
        ST_TIMESTAMP.set(stTimestamp);
    }

    public static Long getStTimestamp() {
        return ST_TIMESTAMP.get();
    }

    public static void clear() {
        ST_TIMESTAMP.remove();
    }

    private RequestContext() {}
}
