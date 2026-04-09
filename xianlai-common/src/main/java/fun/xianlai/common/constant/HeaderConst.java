package fun.xianlai.common.constant;

/**
 * @author WyattLau
 */
public final class HeaderConst {
    public static final String BEGIN_TIME = "X-Begin-Time";         // 网关接收到前端请求生成的请求处理开始时间戳（毫秒）
    public static final String REQUEST_TIME = "X-Request-Time";     // 前端发起请求时间戳（毫秒）
    public static final String TOKEN = "X-Token";                   // 登录后，前端每个请求携带的令牌
    public static final String FROM_SERVICE = "X-From-Service";     // 后端服务之间流转标记的上游服务
    public static final String TENANT_ID = "X-Tenant-ID";           // 网关从token解析出来的tenantId
    public static final String TRACE_ID = "X-Trace-ID";             // 网关生成的traceId
    public static final String SYSTEM_CONFIG_UPDATE_TIME = "X-System-Config-Update-Time";   // 系统配置更新时间戳（毫秒）
    public static final String TENANT_CONFIG_UPDATE_TIME = "X-Tenant-Config-Update-Time";   // 租户配置更新时间戳（毫秒）
    public static final String PATH_UPDATE_TIME = "X-Path-Update-Time";                     // 路径数据更新时间戳（毫秒）
    public static final String ROUTE_UPDATE_TIME = "X-Route-Update-Time";                   // 路由数据更新时间戳（毫秒）

    private HeaderConst() {
    }
}
