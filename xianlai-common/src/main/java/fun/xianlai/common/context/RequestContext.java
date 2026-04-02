package fun.xianlai.common.context;

/**
 * @author WyattLau
 */
public final class RequestContext {
    private static final ThreadLocal<Long> TENANT_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> TRACE_ID = new ThreadLocal<>();
    private static final ThreadLocal<Long> ST_TIMESTAMP = new ThreadLocal<>();

    public static void setTenantId(Long tenantId) {
        TENANT_ID.set(tenantId);
    }

    public static Long getTenantId() {
        return TENANT_ID.get();
    }

    public static void setTraceId(String traceId) {
        TRACE_ID.set(traceId);
    }

    public static String getTraceId() {
        return TRACE_ID.get();
    }

    public static void setStTimestamp(Long stTimestamp) {
        ST_TIMESTAMP.set(stTimestamp);
    }

    public static Long getStTimestamp() {
        return ST_TIMESTAMP.get();
    }

    public static void clear() {
        TENANT_ID.remove();
        TRACE_ID.remove();
        ST_TIMESTAMP.remove();
    }

    private RequestContext() {}
}
