package fun.xianlai.system.core.service;

import fun.xianlai.system.core.model.entity.XLSystemConfig;
import fun.xianlai.system.core.model.entity.XLTenantConfig;

import java.util.List;
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
    Map<String, XLSystemConfig> getSystemConfigs();
}
