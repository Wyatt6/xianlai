package fun.xianlai.system.core.service;

import fun.xianlai.system.core.model.entity.XLTenant;

/**
 * @author WyattLau
 */
public interface TenantService {
    /**
     * 根据域名获取租户
     */
    XLTenant getTenantByDomain(String domain);
}
