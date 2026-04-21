package fun.xianlai.common.constant;

/**
 * @author WyattLau
 */
public final class TenantConst {
    public static final String DOMAIN_CACHE_KEY = "tenant:domain:{0}";
    public static final String ENTITY_CACHE_KEY = "tenant:{0}:entity";
    public static final String CONFIG_VERSION_CACHE_KEY = "tenant:{0}:config:{1}:version";
    public static final String CONFIG_CACHE_KEY = "tenant:{0}:config:{1}:{2}";  // 0-租户ID 1-全局配置版本 2-租户配置版本
    public static final long DEFAULT_CACHE_HOURS = 3L;

    private TenantConst() {
    }
}
