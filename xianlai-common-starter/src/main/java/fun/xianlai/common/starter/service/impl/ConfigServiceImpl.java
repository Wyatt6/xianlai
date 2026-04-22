package fun.xianlai.common.starter.service.impl;

import fun.xianlai.common.constant.ConfigConst;
import fun.xianlai.common.constant.TenantConst;
import fun.xianlai.common.exception.SysException;
import fun.xianlai.common.response.RetCode;
import fun.xianlai.common.starter.pojo.XLConfigPojo;
import fun.xianlai.common.starter.service.ConfigService;
import fun.xianlai.common.utils.bean.BeanUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 配置服务
 * 本地内存 <-- Redis缓存 <-- 数据库
 *
 * @author WyattLau
 */
@Slf4j
public class ConfigServiceImpl implements ConfigService {
    // 全局配置版本（线程安全）
    private final AtomicLong globalVersion = new AtomicLong(0);
    // 全局配置数据（线程安全）
    private final Map<String, Map<String, Object>> globalConfigs = new ConcurrentHashMap<>();

    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public Long getLocalGlobalVersion() {
        return globalVersion.get();
    }

    // ----- 以下用于租户配置的更新 -----

    /**
     * 缓存租户配置
     *
     * @return 租户配置版本
     */
    private Long refreshTenantConfigCache(Long tenantId) {
        Long version = getTenantConfigVersionFromDb(tenantId);

        // 先用全局配置作为默认值兜底，再用租户配置覆盖
        Map<String, Map<String, Object>> configs = new HashMap<>(globalConfigs);
        configs.putAll(getTenantConfigsFromDb(tenantId));

        String versionKey = getTenantConfigVersionCacheKey(tenantId);
        redis.opsForValue().set(versionKey, version, Duration.ofHours(TenantConst.DEFAULT_CACHE_HOURS));
        String configKey = getTenantConfigCacheKey(tenantId, version);
        redis.opsForHash().putAll(configKey, configs);
        redis.expire(configKey, Duration.ofHours(TenantConst.DEFAULT_CACHE_HOURS));
        log.info("缓存租户配置更新完成，租户ID: {}", tenantId);
        return version;
    }

    @Override
    public Long getTenantConfigVersionFromCache(Long tenantId) {
        String key = getTenantConfigVersionCacheKey(tenantId);
        if (!redis.hasKey(key)) {
            refreshTenantConfigCache(tenantId);
        }
        return BeanUtils.parseLong(redis.opsForValue().get(key));
    }

    /**
     * 从缓存查询租户配置
     */
    @Override
    public Map<String, Map<String, Object>> getTenantConfigsFromCache(Long tenantId) {
        Long version = getTenantConfigVersionFromCache(tenantId);
        String key = getTenantConfigCacheKey(tenantId, version);
        if (!redis.hasKey(key)) {
            version = refreshTenantConfigCache(tenantId);
        }
        key = getTenantConfigCacheKey(tenantId, version);
        Map<String, Map<String, Object>> configs = new HashMap<>();
        redis.opsForHash().entries(key).forEach((k, v) -> {
            configs.put(String.valueOf(k), BeanUtils.objectToMap(v));
        });
        return configs;
    }

    /**
     * 从数据库获取租户配置版本
     */
    private Long getTenantConfigVersionFromDb(Long tenantId) {
        return jdbc.queryForObject("select config_version from tb_core_tenant where id = " + tenantId, Long.class);
    }

    /**
     * 从数据库获取租户配置
     */
    private Map<String, Map<String, Object>> getTenantConfigsFromDb(Long tenantId) {
        List<XLConfigPojo> configList = jdbc.query(
                "select * from tb_core_config where belong_id = " + tenantId + " and enabled = 1",
                BeanPropertyRowMapper.newInstance(XLConfigPojo.class)
        );
        Map<String, Map<String, Object>> configMap = new ConcurrentHashMap<>();
        for (XLConfigPojo item : configList) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("value", item.getConfigValue());
            itemMap.put("type", item.getValueType());
            itemMap.put("frontLoad", item.getFrontLoad());
            configMap.put(item.getConfigKey(), itemMap);
        }
        return configMap;
    }

    /**
     * 获取租户配置版本缓存key
     */
    private String getTenantConfigVersionCacheKey(Long tenantId) {
        return MessageFormat.format(TenantConst.CONFIG_VERSION_CACHE_KEY, tenantId, globalVersion.get());
    }

    /**
     * 获取租户配置缓存key
     */
    private String getTenantConfigCacheKey(Long tenantId, Long tenantConfigVersion) {
        return MessageFormat.format(TenantConst.CONFIG_CACHE_KEY, tenantId, globalVersion.get(), tenantConfigVersion);
    }

    // ----- 以下用于全局配置的初始化和同步 -----

    /**
     * 同步全局配置版本和全局配置到本地内存
     */
    @PostConstruct
    public void initGlobalConfig() {
        try {
            globalVersion.set(getGlobalVersionFromCache());
            log.info("本地全局配置版本已加载: {}", globalVersion.get());
            globalConfigs.clear();
            globalConfigs.putAll(getGlobalConfigsFromCache());
            log.info("本地全局配置数据已加载");
        } catch (Exception e) {
            throw new SysException(RetCode.INIT_ERROR, "全局配置初始化失败: " + e.getMessage());
        }
    }

    @Override
    public void globalConfigPollingTask() {
        try {
            Long cacheVersion = getGlobalVersionFromCache();
            Long dbVersion = getGlobalVersionFromDb();
            if (!Objects.equals(cacheVersion, dbVersion)) {
                log.info("缓存和数据库的全局配置版本不一致，更新缓存");
                cacheVersion = refreshGlobalConfigCache();
            }
            if (cacheVersion != null && !cacheVersion.equals(globalVersion.get())) {
                log.info("本地和缓存的全局配置版本不一致，更新本地");
                log.info("本地全局配置版本已更新: {} -> {}", globalVersion.get(), cacheVersion);
                globalVersion.set(cacheVersion);
                globalConfigs.clear();
                globalConfigs.putAll(getGlobalConfigsFromCache());
                log.info("本地全局配置数据已更新");
            }
        } catch (Exception e) {
            log.warn("全局配置同步异常，使用本地数据（版本: {}），异常信息: {}", globalVersion.get(), e.getMessage());
        }
    }

    /**
     * 更新缓存的全局配置
     *
     * @return 全局配置版本
     */
    private Long refreshGlobalConfigCache() {
        Long globalVersion = getGlobalVersionFromDb();
        Map<String, Map<String, Object>> globalConfigs = getGlobalConfigsFromDb();
        redis.opsForValue().set(ConfigConst.GLOBAL_VERSION_CACHE_KEY, globalVersion, Duration.ofHours(ConfigConst.DEFAULT_CACHE_HOURS));
        String key = getGlobalConfigCacheKey(globalVersion);
        redis.opsForHash().putAll(key, globalConfigs);
        redis.expire(key, Duration.ofHours(ConfigConst.DEFAULT_CACHE_HOURS));
        log.info("缓存全局配置更新完成");
        return globalVersion;
    }

    /**
     * 从缓存查询全局配置版本
     */
    private Long getGlobalVersionFromCache() {
        if (!redis.hasKey(ConfigConst.GLOBAL_VERSION_CACHE_KEY)) {
            refreshGlobalConfigCache();
        }
        return BeanUtils.parseLong(redis.opsForValue().get(ConfigConst.GLOBAL_VERSION_CACHE_KEY));
    }

    /**
     * 从缓存查询全局配置
     */
    private Map<String, Map<String, Object>> getGlobalConfigsFromCache() {
        Long globalVersion = getGlobalVersionFromCache();
        String key = getGlobalConfigCacheKey(globalVersion);
        if (!redis.hasKey(key)) {
            globalVersion = refreshGlobalConfigCache();
        }
        key = getGlobalConfigCacheKey(globalVersion);
        Map<String, Map<String, Object>> globalConfigs = new ConcurrentHashMap<>();
        redis.opsForHash().entries(key).forEach((k, v) -> {
            globalConfigs.put(String.valueOf(k), BeanUtils.objectToMap(v));
        });
        return globalConfigs;
    }

    /**
     * 从数据库查询全局配置版本
     */
    private Long getGlobalVersionFromDb() {
        return jdbc.queryForObject("select config_value from tb_core_config where belong_id = 0 and config_key = 'globalVersion'", Long.class);
    }

    /**
     * 从数据库查询全局配置
     */
    private Map<String, Map<String, Object>> getGlobalConfigsFromDb() {
        List<XLConfigPojo> configList = jdbc.query(
                "select * from tb_core_config where belong_id = 0 and enabled = 1",
                BeanPropertyRowMapper.newInstance(XLConfigPojo.class)
        );
        Map<String, Map<String, Object>> configMap = new ConcurrentHashMap<>();
        for (XLConfigPojo item : configList) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("value", item.getConfigValue());
            itemMap.put("type", item.getValueType());
            itemMap.put("frontLoad", item.getFrontLoad());
            configMap.put(item.getConfigKey(), itemMap);
        }
        return configMap;
    }

    /**
     * 获取全局配置缓存key
     */
    private String getGlobalConfigCacheKey(Long version) {
        return MessageFormat.format(ConfigConst.GLOBAL_CONFIG_CACHE_KEY, version);
    }
}
