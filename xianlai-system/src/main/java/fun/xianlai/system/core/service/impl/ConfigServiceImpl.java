package fun.xianlai.system.core.service.impl;

import fun.xianlai.common.annotation.ServiceLog;
import fun.xianlai.common.annotation.SimpleServiceLog;
import fun.xianlai.system.core.model.entity.XLSystemConfig;
import fun.xianlai.system.core.model.entity.XLTenantConfig;
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
import java.util.ArrayList;
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

    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private XLSystemConfigRepository systemConfigRepository;
    @Lazy
    @Autowired
    private ConfigService self;

    @Override
    @SimpleServiceLog("缓存系统配置")
    public void cacheSystemConfigs() {
        List<XLSystemConfig> configList = systemConfigRepository.findByEnabled(true);
        Map<String, XLSystemConfig> configMap = new HashMap<>();
        for (XLSystemConfig item : configList) {
            configMap.put(item.getConfigKey(), item);
        }
        redis.opsForHash().putAll(SYSTEM_CONFIG_KEY, configMap);
        redis.expire(SYSTEM_CONFIG_KEY, Duration.ofHours(SYSTEM_CONFIG_CACHE_HOURS));
    }

    @Override
    @SimpleServiceLog("获取系统配置")
    public Map<String, XLSystemConfig> getSystemConfigs() {
        if (!redis.hasKey(SYSTEM_CONFIG_KEY)) {
            this.cacheSystemConfigs();
        } else {
            redis.expire(SYSTEM_CONFIG_KEY, Duration.ofHours(SYSTEM_CONFIG_CACHE_HOURS));
        }
        Map<Object, Object> cachedConfigs = redis.opsForHash().entries(SYSTEM_CONFIG_KEY);
        Map<String, XLSystemConfig> configs = new HashMap<>();
        if (!cachedConfigs.isEmpty()) {
            cachedConfigs.forEach((key, value) -> {
                configs.put(String.valueOf(key), (XLSystemConfig) value);
            });
        }
        return configs;
    }
}
