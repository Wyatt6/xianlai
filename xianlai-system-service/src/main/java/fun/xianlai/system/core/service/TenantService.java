package fun.xianlai.system.core.service;

import fun.xianlai.system.core.entity.XLTenant;

/**
 * @author WyattLau
 */
public interface TenantService {
    /**
     * 根据域名获取租户
     */
    XLTenant getTenantByDomain(String domain);

    /**
     * 根据ID获取租户
     */
    XLTenant getTenantById(Long id);
}
