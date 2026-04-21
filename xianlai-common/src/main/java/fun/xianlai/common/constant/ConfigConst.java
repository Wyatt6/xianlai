package fun.xianlai.common.constant;

/**
 * @author WyattLau
 */
public final class ConfigConst {
    public static final String GLOBAL_VERSION_CACHE_KEY = "globalConfig:version";
    public static final String GLOBAL_CONFIG_CACHE_KEY = "globalConfig:{0}";
    public static final long DEFAULT_CACHE_HOURS = 72L; // 3天

    public static final String CONFIG_CACHE_KEY = "system:config";
    public static final String CONFIG_UPDATE_TIME_CONFIG_KEY = "systemConfigUpdateTime";

    private ConfigConst() {}
}
