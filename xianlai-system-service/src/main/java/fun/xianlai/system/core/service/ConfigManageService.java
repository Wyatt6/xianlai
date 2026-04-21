package fun.xianlai.system.core.service;

import java.util.Map;

/**
 * @author WyattLau
 */
public interface ConfigManageService {
    /**
     * 获取租户加载到前端的配置
     */
    Map<String, Map<String, Object>> getTenantFrontLoadConfigs(Long tenantId);
}
