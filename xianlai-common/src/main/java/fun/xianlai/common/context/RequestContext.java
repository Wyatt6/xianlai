package fun.xianlai.common.context;

/**
 * @author WyattLau
 */
public final class RequestContext {
    private static final ThreadLocal<String> FROM_SERVICE = new ThreadLocal<>();
    private static final ThreadLocal<Long> TENANT_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> TRACE_ID = new ThreadLocal<>();
    private static final ThreadLocal<Long> BEGIN_TIME = new ThreadLocal<>();        // 内部调用的时候不是必须的

    public static void setFromService(String fromService) {
        FROM_SERVICE.set(fromService);
    }

    public static String getFromService() {
        return FROM_SERVICE.get();
    }

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

    public static void setBeginTime(Long beginTime) {
        BEGIN_TIME.set(beginTime);
    }

    public static Long getBeginTime() {
        return BEGIN_TIME.get();
    }

    public static void clear() {
        FROM_SERVICE.remove();
        TENANT_ID.remove();
        TRACE_ID.remove();
        BEGIN_TIME.remove();
    }

    private RequestContext() {}
}
