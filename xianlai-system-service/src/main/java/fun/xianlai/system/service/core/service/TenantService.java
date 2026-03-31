package fun.xianlai.system.service.core.service;

import fun.xianlai.system.service.core.entity.XLTenant;

/**
 * @author WyattLau
 */
public interface TenantService {
    /**
     * 根据域名获取租户
     */
    XLTenant getTenantByDomain(String domain);
}
