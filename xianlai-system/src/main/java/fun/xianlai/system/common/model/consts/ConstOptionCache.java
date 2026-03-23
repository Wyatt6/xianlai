package fun.xianlai.system.common.model.consts;

/**
 * @author WyattLau
 */
public class ConstOptionCache {
    public static final long SYSTEM_OPTION_CACHE_HOURS = 720L;  // 30天
    public static final String SYSTEM_OPTION_CACHE_KEY = "systemOptions";
    public static final String SYSTEM_OPTION_CHECKSUM_CACHE_KEY = "systemOptionsChecksum";

    public static final long TENANT_OPTION_CACHE_HOURS = 240L;  // 10天
    public static final String TENANT_OPTION_CACHE_KEY_PREFIX = "tenantOptions:";
    public static final String TENANT_OPTION_CHECKSUM_CACHE_KEY_PREFIX = "tenantOptionsChecksum:";

    public static final long USER_OPTION_CACHE_HOURS = 72L;     // 3天
    public static final String USER_OPTION_CACHE_KEY_PREFIX = "userOptions:";
    public static final String USER_OPTION_CHECKSUM_CACHE_KEY_PREFIX = "userOptionsChecksum:";

    public static final String SINGLE_OPTION_CACHE_KEY_PREFIX = "option:";
}
