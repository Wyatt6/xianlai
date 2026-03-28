package fun.xianlai.common.response;

/**
 * 响应状态码
 * 大体上复用HTTP状态码，业务细分码在其前缀的基础上增加
 *
 * @author WyattLau
 */
public class RetCode {
    // 成功（复用HTTP状态码200）
    public static final String SUCCESS = "200";

    // 业务错误（基于HTTP状态码400扩展）
    public static final String BIZ_ERROR = "400";                           // 通用业务错误
    public static final String PARAM_ERROR = "400-001";                     // 请求参数校验失败
    public static final String DATA_NOT_FOUND = "400-002";                  // 数据不存在
    public static final String DATA_ALREADY_EXISTS = "400-003";             // 数据已存在

    // 认证失败/未登录（复用HTTP状态码401）
    public static final String UNAUTHORIZED = "401";

    // 权限不足（复用HTTP状态码403）
    public static final String FORBIDDEN = "403";

    // 系统错误（基于HTTP状态码500扩展）
    public static final String SYS_ERROR = "500";                           // 通用系统错误
    public static final String DB_ERROR = "500-001";                        // 数据库异常
    public static final String CACHE_ERROR = "500-002";                     // 缓存异常
    public static final String THIRD_SERVICE_ERROR = "500-003";             // 第三方服务异常
}
