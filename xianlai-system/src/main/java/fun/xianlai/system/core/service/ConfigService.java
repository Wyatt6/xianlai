package fun.xianlai.system.core.service;

import java.util.Map;

/**
 * @author WyattLau
 */
public interface ConfigService {
    /**
     * 缓存全量系统配置
     */
    void cacheSystemConfigs();

    /**
     * 获取全量系统配置
     */
    Map<String, Map<String, Object>> getSystemConfigs();

    /**
     * 缓存全量租户配置
     */
    void cacheTenantConfigs(Long tenantId);

    /**
     * 获取全量租户配置
     */
    Map<String, Map<String, Object>> getTenantConfigs(Long tenantId);

    /**
     * 获取租户加载到前端的全量配置（含系统配置和租户配置）
     */
    Map<String, Map<String, Object>> getFrontLoadConfigsOfTenant(Long tenantId);
}
