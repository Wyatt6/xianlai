package fun.xianlai.system.core.service.impl;

import fun.xianlai.common.constant.SystemConst;
import fun.xianlai.common.constant.TenantConst;
import fun.xianlai.common.enums.EConfigScope;
import fun.xianlai.common.utils.bean.BeanUtils;
import fun.xianlai.system.core.entity.XLSystemConfig;
import fun.xianlai.system.core.entity.XLTenantConfig;
import fun.xianlai.system.core.repository.XLSystemConfigRepository;
import fun.xianlai.system.core.repository.XLTenantConfigRepository;
import fun.xianlai.system.core.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private XLSystemConfigRepository systemConfigRepository;
    @Autowired
    private XLTenantConfigRepository tenantConfigRepository;

    @Override
    public void cacheSystemConfigs() {
        List<XLSystemConfig> configList = systemConfigRepository.findByEnabled(true);

        Map<String, Map<String, Object>> allConfigs = new HashMap<>();
        for (XLSystemConfig item : configList) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("value", item.getConfigValue());
            itemMap.put("type", item.getValueType());
            itemMap.put("scope", item.getScope());
            itemMap.put("frontLoad", item.getFrontLoad());
            allConfigs.put(item.getConfigKey(), itemMap);
        }

        redis.opsForHash().putAll(SystemConst.CONFIG_CACHE_KEY, allConfigs);
        redis.expire(SystemConst.CONFIG_CACHE_KEY, Duration.ofHours(SystemConst.DEFAULT_CACHE_HOURS));
        log.info("系统配置缓存完成");
    }

    @Override
    public Map<String, Map<String, Object>> getSystemConfigs() {
        if (redis.hasKey(SystemConst.CONFIG_CACHE_KEY)) {
            redis.expire(SystemConst.CONFIG_CACHE_KEY, Duration.ofHours(SystemConst.DEFAULT_CACHE_HOURS));
        } else {
            this.cacheSystemConfigs();
        }
        Map<String, Map<String, Object>> configs = new HashMap<>();
        redis.opsForHash().entries(SystemConst.CONFIG_CACHE_KEY).forEach((k, v) -> {
            configs.put(String.valueOf(k), BeanUtils.objectToMap(v));
        });
        return configs;
    }

    @Override
    public void cacheTenantConfigs(Long tenantId) {
        Map<String, Map<String, Object>> allConfigs = new HashMap<>();
        Map<String, Map<String, Object>> frontLoadConfigs = new HashMap<>();

        // 先继承系统配置中作用域是TENANT和USER的项
        Map<String, Map<String, Object>> systemAllConfigs = this.getSystemConfigs();
        systemAllConfigs.forEach((k, v) -> {
            String scope = (String) v.remove("scope");
            switch (scope) {
                case EConfigScope.TENANT, EConfigScope.USER -> allConfigs.put(k, v);
            }
            if ((Boolean) v.get("frontLoad")) {
                switch (scope) {
                    case EConfigScope.TENANT, EConfigScope.USER -> frontLoadConfigs.put(k, v);
                }
            }
        });

        // 查数据库获取租户配置数据，覆盖默认配置（系统配置）
        List<XLTenantConfig> tenantConfigList = tenantConfigRepository.findByBelongToAndEnabled(tenantId, true);
        for (XLTenantConfig item : tenantConfigList) {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("value", item.getConfigValue());
            map1.put("type", item.getValueType());
            allConfigs.put(item.getConfigKey(), map1);

            if (item.getFrontLoad()) {
                Map<String, Object> map2 = new HashMap<>();
                map2.put("value", item.getConfigValue());
                map2.put("type", item.getValueType());
                frontLoadConfigs.put(item.getConfigKey(), map2);
            }
        }

        String keyAll = MessageFormat.format(TenantConst.CONFIG_ALL_CACHE_KEY, tenantId);
        String keyFrontLoad = MessageFormat.format(TenantConst.CONFIG_FRONT_LOAD_CACHE_KEY, tenantId);
        redis.opsForHash().putAll(keyAll, allConfigs);
        redis.opsForHash().putAll(keyFrontLoad, frontLoadConfigs);
        redis.expire(keyAll, Duration.ofHours(TenantConst.DEFAULT_CACHE_HOURS));
        redis.expire(keyFrontLoad, Duration.ofHours(TenantConst.DEFAULT_CACHE_HOURS));
        log.info("租户配置缓存完成");
    }

    @Override
    public Map<String, Map<String, Object>> getTenantFrontLoadConfigs(Long tenantId) {
        String keyAll = MessageFormat.format(TenantConst.CONFIG_ALL_CACHE_KEY, tenantId);
        String keyFrontLoad = MessageFormat.format(TenantConst.CONFIG_FRONT_LOAD_CACHE_KEY, tenantId);

        if (redis.hasKey(keyFrontLoad)) {
            redis.expire(keyAll, Duration.ofHours(TenantConst.DEFAULT_CACHE_HOURS));
            redis.expire(keyFrontLoad, Duration.ofHours(TenantConst.DEFAULT_CACHE_HOURS));
        } else {
            this.cacheTenantConfigs(tenantId);
        }

        Map<String, Map<String, Object>> configs = new HashMap<>();
        redis.opsForHash().entries(keyFrontLoad).forEach((k, v) -> {
            configs.put(String.valueOf(k), BeanUtils.objectToMap(v));
        });
        return configs;
    }
}
