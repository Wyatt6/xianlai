package fun.xianlai.system.core.service.impl;

import fun.xianlai.common.annotation.ServiceLog;
import fun.xianlai.common.annotation.SimpleServiceLog;
import fun.xianlai.common.utils.bean.BeanUtils;
import fun.xianlai.system.core.model.entity.XLSystemConfig;
import fun.xianlai.system.core.model.entity.XLTenantConfig;
import fun.xianlai.system.core.model.enums.EConfigScope;
import fun.xianlai.system.core.repository.XLSystemConfigRepository;
import fun.xianlai.system.core.repository.XLTenantConfigRepository;
import fun.xianlai.system.core.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class ConfigServiceImpl implements ConfigService {
    private static final String SYSTEM_CONFIG_KEY = "system:config";
    private static final long SYSTEM_CONFIG_CACHE_HOURS = 6L;

    private static final String TENANT_CONFIG_KEY = "tenant:config:{0}";
    private static final long TENANT_CONFIG_CACHE_HOURS = 3L;

    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private XLSystemConfigRepository systemConfigRepository;
    @Autowired
    private XLTenantConfigRepository tenantConfigRepository;
    @Lazy
    @Autowired
    private ConfigService self;

    @Override
    @SimpleServiceLog("缓存全量系统配置")
    public void cacheSystemConfigs() {
        List<XLSystemConfig> configList = systemConfigRepository.findByEnabled(true);
        Map<String, Map<String, Object>> configMap = new HashMap<>();
        for (XLSystemConfig item : configList) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("scope", item.getScope());
            itemMap.put("value", item.getConfigValue());
            itemMap.put("type", item.getValueType());
            itemMap.put("frontLoad", item.getFrontLoad());
            configMap.put(item.getConfigKey(), itemMap);
        }
        redis.opsForHash().putAll(SYSTEM_CONFIG_KEY, configMap);
        redis.expire(SYSTEM_CONFIG_KEY, Duration.ofHours(SYSTEM_CONFIG_CACHE_HOURS));
    }

    @Override
    @SimpleServiceLog("获取全量系统配置")
    public Map<String, Map<String, Object>> getSystemConfigs() {
        if (!redis.hasKey(SYSTEM_CONFIG_KEY)) {
            self.cacheSystemConfigs();
        } else {
            redis.expire(SYSTEM_CONFIG_KEY, Duration.ofHours(SYSTEM_CONFIG_CACHE_HOURS));
        }
        Map<String, Map<String, Object>> configs = new HashMap<>();
        redis.opsForHash().entries(SYSTEM_CONFIG_KEY).forEach((k, v) -> {
            configs.put(String.valueOf(k), BeanUtils.objectToMap(v));
        });
        return configs;
    }

    @Override
    @ServiceLog("缓存全量租户配置")
    public void cacheTenantConfigs(Long tenantId) {
        Map<String, Map<String, Object>> tenantConfigs = new HashMap<>();
        log.info("先继承系统配置中作用域是TENANT和USER的项");
        Map<String, Map<String, Object>> systemConfigs = self.getSystemConfigs();
        systemConfigs.forEach((k, v) -> {
            String scope = String.valueOf(v.get("scope"));
            if (EConfigScope.TENANT.equals(scope) || EConfigScope.USER.equals(scope)) {
                tenantConfigs.put(k, v);
            }
        });
        log.info("查数据库获取租户配置数据，再覆盖系统配置");
        String key = MessageFormat.format(TENANT_CONFIG_KEY, tenantId);
        List<XLTenantConfig> tenantConfigList = tenantConfigRepository.findByScopeIdAndEnabled(tenantId, true);
        for (XLTenantConfig item : tenantConfigList) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("scope", item.getScope());
            itemMap.put("value", item.getConfigValue());
            itemMap.put("type", item.getValueType());
            itemMap.put("frontLoad", item.getFrontLoad());
            tenantConfigs.put(item.getConfigKey(), itemMap);
        }
        redis.opsForHash().putAll(key, tenantConfigs);
        redis.expire(key, Duration.ofHours(TENANT_CONFIG_CACHE_HOURS));
    }

    @Override
    @SimpleServiceLog("获取全量租户配置")
    public Map<String, Map<String, Object>> getTenantConfigs(Long tenantId) {
        String key = MessageFormat.format(TENANT_CONFIG_KEY, tenantId);
        if (!redis.hasKey(key)) {
            self.cacheTenantConfigs(tenantId);
        } else {
            redis.expire(key, Duration.ofHours(TENANT_CONFIG_CACHE_HOURS));
        }
        Map<Object, Object> cachedConfigs = redis.opsForHash().entries(key);
        Map<String, Map<String, Object>> configs = new HashMap<>();
        if (!cachedConfigs.isEmpty()) {
            cachedConfigs.forEach((k, v) -> {
                configs.put(String.valueOf(k), BeanUtils.objectToMap(v));
            });
        }
        return configs;
    }

    @Override
    @ServiceLog("获取租户加载到前端的全量配置")
    public Map<String, Map<String, Object>> getFrontLoadConfigsOfTenant(Long tenantId) {
        Map<String, Map<String, Object>> systemConfigs = self.getSystemConfigs();
        Map<String, Map<String, Object>> tenantConfigs = self.getTenantConfigs(tenantId);
        Map<String, Map<String, Object>> frontLoadConfigs = new HashMap<>();
        log.info("筛选出加载到前端的系统配置");
        systemConfigs.forEach((k, v) -> {
            if (Boolean.parseBoolean(String.valueOf(v.get("frontLoad")))) {
                frontLoadConfigs.put(k, v);
            }
        });
        log.info("筛选出加载到前端的租户配置，并覆盖系统配置（如有）");
        tenantConfigs.forEach((k, v) -> {
            if (Boolean.parseBoolean(String.valueOf(v.get("frontLoad")))) {
                frontLoadConfigs.put(k, v);
            }
        });
        return frontLoadConfigs;
    }
}
