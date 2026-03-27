package fun.xianlai.system.core.service;

import java.util.Map;

/**
 * @author WyattLau
 */
public interface ConfigService {
    /**
     * 缓存系统配置
     */
    void cacheSystemConfigs();

    /**
     * 获取系统配置
     */
    Map<String, Map<String, Object>> getSystemConfigs();

    /**
     * 缓存租户配置
     */
    void cacheTenantConfigs(Long tenantId);

    /**
     * 获取租户配置
     */
    Map<String, Map<String, Object>> getTenantConfigs(Long tenantId);
}
