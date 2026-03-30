package fun.xianlai.common.context;

/**
 * @author WyattLau
 */
public class RequestContext {
    private static final ThreadLocal<Long> TRACE_ID = new ThreadLocal<>();
    private static final ThreadLocal<Long> ST_TIMESTAMP = new ThreadLocal<>();

    public static void setTraceId(Long traceId) {
        TRACE_ID.set(traceId);
    }

    public static Long getTraceId() {
        return TRACE_ID.get();
    }

    public static void setStTimestamp(Long stTimestamp) {
        ST_TIMESTAMP.set(stTimestamp);
    }

    public static Long getStTimestamp() {
        return ST_TIMESTAMP.get();
    }

    public static void clear() {
        TRACE_ID.remove();
        ST_TIMESTAMP.remove();
    }
}
