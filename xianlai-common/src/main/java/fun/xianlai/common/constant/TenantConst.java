package fun.xianlai.common.constant;

/**
 * @author WyattLau
 */
public final class TenantConst {
    public static final String DOMAIN_CACHE_KEY = "tenant:domain:{0}";
    public static final String ENTITY_CACHE_KEY = "tenant:{0}:entity";
    private static final String CONFIG_CACHE_KEY = "tenant:{0}:config";
    public static final long DEFAULT_CACHE_HOURS = 3L;

    private TenantConst() {}
}
