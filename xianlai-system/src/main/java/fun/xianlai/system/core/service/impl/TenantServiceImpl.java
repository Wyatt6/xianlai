package fun.xianlai.system.core.service.impl;

import fun.xianlai.common.annotation.SimpleServiceLog;
import fun.xianlai.common.exception.SysException;
import fun.xianlai.system.core.model.entity.XLTenant;
import fun.xianlai.system.core.repository.XLTenantRepository;
import fun.xianlai.system.core.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Service
public class TenantServiceImpl implements TenantService {
    private static final String TENANT_DOMAIN_KEY = "tenant:domain:";
    private static final long TENANT_DOMAIN_CACHE_HOURS = 3L;

    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private XLTenantRepository tenantRepository;

    @Override
    @SimpleServiceLog("根据域名获取租户")
    public XLTenant getTenantByDomain(String domain) {
        String key = TENANT_DOMAIN_KEY + domain;

        XLTenant cachedTenant = (XLTenant) redis.opsForValue().get(key);
        if (cachedTenant != null) {
            redis.expire(key, Duration.ofHours(TENANT_DOMAIN_CACHE_HOURS));
            return cachedTenant;
        }

        Optional<XLTenant> tenant = tenantRepository.findByDomain(domain);
        if (tenant.isEmpty()) {
            String subDomain = getFirstSubDomain(domain);
            tenant = tenantRepository.findByCode(subDomain);
        }
        if (tenant.isPresent()) {
            redis.opsForValue().set(key, tenant.get(), Duration.ofHours(TENANT_DOMAIN_CACHE_HOURS));
            return tenant.get();
        } else {
            throw new SysException("未找到对应租户，请检查域名是否正确");
        }
    }

    /**
     * 提取最小的子域名（前缀）
     * abc.company.com --> abc
     * abc.xyz.company.com --> abc
     */
    private String getFirstSubDomain(String domain) {
        return domain.split("\\.")[0];
    }
}
