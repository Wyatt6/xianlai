package fun.xianlai.system.core.service.impl;

import fun.xianlai.common.constant.ConfigConst;
import fun.xianlai.common.constant.TenantConst;
import fun.xianlai.common.exception.BizException;
import fun.xianlai.common.response.RetCode;
import fun.xianlai.common.utils.bean.BeanUtils;
import fun.xianlai.system.core.entity.XLSystemConfig;
import fun.xianlai.system.core.entity.XLTenantConfig;
import fun.xianlai.system.core.repository.XLSystemConfigRepository;
import fun.xianlai.system.core.repository.XLTenantConfigRepository;
import fun.xianlai.system.core.service.ConfigManageService;
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
public class ConfigManageServiceImpl implements ConfigManageService {
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
            itemMap.put("frontLoad", item.getFrontLoad());
            allConfigs.put(item.getConfigKey(), itemMap);
        }

        redis.opsForHash().putAll(ConfigConst.CONFIG_CACHE_KEY, allConfigs);
        redis.expire(ConfigConst.CONFIG_CACHE_KEY, Duration.ofHours(ConfigConst.DEFAULT_CACHE_HOURS));
        log.info("系统配置缓存完成");
    }

    @Override
    public Map<String, Map<String, Object>> getSystemConfigs() {
        if (!redis.hasKey(ConfigConst.CONFIG_CACHE_KEY)) {
            this.cacheSystemConfigs();
        }
        Map<String, Map<String, Object>> configs = new HashMap<>();
        redis.opsForHash().entries(ConfigConst.CONFIG_CACHE_KEY).forEach((k, v) -> {
            configs.put(String.valueOf(k), BeanUtils.objectToMap(v));
        });
        return configs;
    }

    @Override
    public void cacheTenantConfigs(Long tenantId) {
        // 先继承所有系统配置作为兜底的默认配置
        Map<String, Map<String, Object>> allConfigs = this.getSystemConfigs();

        // 查数据库获取租户配置数据，覆盖默认配置（系统配置）
        List<XLTenantConfig> tenantConfigList = tenantConfigRepository.findByBelongToAndEnabled(tenantId, true);
        for (XLTenantConfig item : tenantConfigList) {
            String itemKey = item.getConfigKey();
            if (allConfigs.containsKey(itemKey)) {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("value", item.getConfigValue());
                itemMap.put("type", item.getValueType());
                itemMap.put("frontLoad", item.getFrontLoad());
                allConfigs.put(itemKey, itemMap);
            }
        }

        // 筛选出前端加载的配置
        Map<String, Map<String, Object>> frontLoadConfigs = new HashMap<>();
        allConfigs.forEach((k, v) -> {
            if ((Boolean) v.remove("frontLoad")) {
                frontLoadConfigs.put(k, v);
            }
        });

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
        if (!redis.hasKey(keyFrontLoad)) {
            this.cacheTenantConfigs(tenantId);
        } else {
            if (redis.hasKey(ConfigConst.CONFIG_CACHE_KEY)) {
                String SCUTString = (String) BeanUtils.objectToMap(redis.opsForHash().get(ConfigConst.CONFIG_CACHE_KEY, ConfigConst.CONFIG_UPDATE_TIME_CONFIG_KEY)).get("value");
                String SCUTStringInTenantCache = (String) BeanUtils.objectToMap(redis.opsForHash().get(keyAll, ConfigConst.CONFIG_UPDATE_TIME_CONFIG_KEY)).get("value");
                if (!SCUTStringInTenantCache.equals(SCUTString)) {
                    this.cacheTenantConfigs(tenantId);
                }
            }
        }

        Map<String, Map<String, Object>> configs = new HashMap<>();
        redis.opsForHash().entries(keyFrontLoad).forEach((k, v) -> {
            configs.put(String.valueOf(k), BeanUtils.objectToMap(v));
        });
        return configs;
    }

    @Override
    public <T> T getConfig(Long tenantId, String key, Class<T> clazz) {
        String keyAll = MessageFormat.format(TenantConst.CONFIG_ALL_CACHE_KEY, tenantId);
        if (!redis.hasKey(keyAll)) {
            this.cacheTenantConfigs(tenantId);
        } else {
            if (redis.hasKey(ConfigConst.CONFIG_CACHE_KEY)) {
                String SCUTString = (String) BeanUtils.objectToMap(redis.opsForHash().get(ConfigConst.CONFIG_CACHE_KEY, ConfigConst.CONFIG_UPDATE_TIME_CONFIG_KEY)).get("value");
                String SCUTStringInTenantCache = (String) BeanUtils.objectToMap(redis.opsForHash().get(keyAll, ConfigConst.CONFIG_UPDATE_TIME_CONFIG_KEY)).get("value");
                if (!SCUTStringInTenantCache.equals(SCUTString)) {
                    this.cacheTenantConfigs(tenantId);
                }
            }
        }

        String value = (String) BeanUtils.objectToMap(redis.opsForHash().get(keyAll, key)).get("value");
        if (value == null) return null;
        try {
            if (clazz == String.class) return clazz.cast(value);
            if (clazz == Integer.class) return clazz.cast(Integer.valueOf(value.trim()));
            if (clazz == Long.class) return clazz.cast(Long.valueOf(value.trim()));
            if (clazz == Boolean.class) return clazz.cast(Boolean.valueOf(value.trim()));
            if (clazz == Double.class) return clazz.cast(Double.valueOf(value.trim()));
            throw new BizException(RetCode.NOT_SUPPORT, "不支持的数据类型: " + clazz.getSimpleName());
        } catch (ClassCastException e) {
            throw new BizException(RetCode.DATA_FORMAT_ERROR, "数据格式错误，无法转换为" + clazz.getSimpleName());
        }
    }

    @Override
    public <T> T getConfigOrDefault(Long tenantId, String key, Class<T> clazz, T defaultValue) {
        T value = this.getConfig(tenantId, key, clazz);
        return value == null ? defaultValue : value;
    }
}
