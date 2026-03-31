package fun.xianlai.system.service.core.service.impl;

import fun.xianlai.common.constant.TenantConst;
import fun.xianlai.common.exception.BizException;
import fun.xianlai.common.response.RetCode;
import fun.xianlai.system.service.core.entity.XLTenant;
import fun.xianlai.system.service.core.repository.XLTenantRepository;
import fun.xianlai.system.service.core.service.TenantService;
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
    public XLTenant getTenantByDomain(String domain) {
        String domainKey = MessageFormat.format(TenantConst.DOMAIN_CACHE_KEY, domain);
        // 根据域名查询缓存
        Long cachedId = (Long) redis.opsForValue().get(domainKey);
        if (cachedId != null) {
            String entityKey = MessageFormat.format(TenantConst.ENTITY_CACHE_KEY, cachedId);
            XLTenant cachedEntity = (XLTenant) redis.opsForValue().get(entityKey);
            if (cachedEntity != null) {
                redis.expire(domainKey, Duration.ofHours(TenantConst.DEFAULT_CACHE_HOURS));
                redis.expire(entityKey, Duration.ofHours(TenantConst.DEFAULT_CACHE_HOURS));
                log.info("成功从缓存查询到租户数据: domain={}, tenantId={}", domain, cachedId);
                return cachedEntity;
            }
        }
        // 缓存查不到租户数据，再根据域名查询数据库
        Optional<XLTenant> tenant = tenantRepository.findByDomain(domain);
        if (tenant.isEmpty()) {
            // 域名查不到租户数据，再根据最小子域名查询数据库
            String subDomain = getFirstSubDomain(domain);
            tenant = tenantRepository.findByCode(subDomain);
        }
        if (tenant.isPresent()) {
            // 成功从数据库查到租户数据，更新缓存
            Long tenantId = tenant.get().getId();
            String entityKey = MessageFormat.format(TenantConst.ENTITY_CACHE_KEY, tenantId);
            redis.opsForValue().set(domainKey, tenantId, Duration.ofHours(TenantConst.DEFAULT_CACHE_HOURS));
            redis.opsForValue().set(entityKey, tenant.get(), Duration.ofHours(TenantConst.DEFAULT_CACHE_HOURS));
            log.info("成功从数据库查询到租户数据: domain={}, tenantId={}", domain, cachedId);
            return tenant.get();
        } else {
            throw new BizException(RetCode.DATA_NOT_FOUND, "未找到对应租户，请检查域名是否正确");
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
