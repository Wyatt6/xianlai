package fun.xianlai.common.starter.service;

import java.util.Map;

/**
 * @author WyattLau
 */
public interface ConfigService {
    /**
     * 全局配置轮询任务（包括版本和配置数据）
     */
    void globalConfigPollingTask();

    /**
     * 从缓存获取租户配置
     */
    Map<String, Map<String, Object>> getTenantConfigsFromCache(Long tenantId);

    /**
     * 获取本地全局配置版本
     */
    Long getLocalGlobalVersion();

    /**
     * 从缓存查询租户配置版本
     */
    Long getTenantConfigVersionFromCache(Long tenantId);
}
