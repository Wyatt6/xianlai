package fun.xianlai.system.core.service.impl;

import fun.xianlai.common.annotation.ServiceLog;
import fun.xianlai.common.constant.CacheKey;
import fun.xianlai.common.exception.SysException;
import fun.xianlai.system.core.model.entity.XLTenant;
import fun.xianlai.system.core.repository.XLTenantRepository;
import fun.xianlai.system.core.service.TenantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private XLTenantRepository tenantRepository;

    @Override
    @ServiceLog("根据域名获取租户")
    public XLTenant getTenantByDomain(String domain) {
        String domainKey = MessageFormat.format(CacheKey.TENANT_DOMAIN_KEY, domain);
        log.info("根据域名查询缓存");
        Long cachedId = (Long) redis.opsForValue().get(domainKey);
        if (cachedId != null) {
            redis.expire(domainKey, Duration.ofHours(CacheKey.TENANT_DEFAULT_CACHE_HOURS));
            String entityKey = MessageFormat.format(CacheKey.TENANT_ENTITY_KEY, cachedId);
            XLTenant cachedEntity = (XLTenant) redis.opsForValue().get(entityKey);
            if (cachedEntity != null) {
                redis.expire(entityKey, Duration.ofHours(CacheKey.TENANT_DEFAULT_CACHE_HOURS));
                return cachedEntity;
            }
        }
        log.info("缓存查不到租户数据，再根据域名查询数据库");
        Optional<XLTenant> tenant = tenantRepository.findByDomain(domain);
        if (tenant.isEmpty()) {
            log.info("域名查不到租户数据，再根据最小子域名查询数据库");
            String subDomain = getFirstSubDomain(domain);
            tenant = tenantRepository.findByCode(subDomain);
        }
        if (tenant.isPresent()) {
            log.info("成功从数据库查到租户数据，更新缓存");
            Long tenantId = tenant.get().getId();
            String entityKey = MessageFormat.format(CacheKey.TENANT_ENTITY_KEY, tenantId);
            redis.opsForValue().set(domainKey, tenantId, Duration.ofHours(CacheKey.TENANT_DEFAULT_CACHE_HOURS));
            redis.opsForValue().set(entityKey, tenant.get(), Duration.ofHours(CacheKey.TENANT_DEFAULT_CACHE_HOURS));
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
