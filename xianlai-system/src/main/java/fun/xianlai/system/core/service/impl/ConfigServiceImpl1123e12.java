package fun.xianlai.system.core.service.impl;

/**
 * @author WyattLau
 */
//@Slf4j
//@Service
public class ConfigServiceImpl1123e12 {
//    @Override
//    public Map<String, Map<String, Object>> getSystemConfigs() {

//    }
//
//    @Override
//    public Object getSystemConfigValueByKey(String key) {
//        if (!redis.hasKey(SystemConst.SYSTEM_CONFIG_KEY)) {
//            self.cacheSystemConfigs();
//        } else {
//            redis.expire(SystemConst.SYSTEM_CONFIG_KEY, Duration.ofHours(SystemConst.SYSTEM_DEFAULT_CACHE_HOURS));
//        }
//        return redis.opsForHash().get(SystemConst.SYSTEM_CONFIG_KEY, key);
//    }
//
//    @Override
//    public void cacheTenantConfigs(Long tenantId) {

//    }
//
//    @Override
//    public Map<String, Map<String, Object>> getTenantConfigs(Long tenantId) {
//        String key = MessageFormat.format(TENANT_CONFIG_KEY, tenantId);
//        if (!redis.hasKey(key)) {
//            self.cacheTenantConfigs(tenantId);
//        } else {
//            redis.expire(key, Duration.ofHours(TENANT_CONFIG_CACHE_HOURS));
//        }
//        Map<Object, Object> cachedConfigs = redis.opsForHash().entries(key);
//        Map<String, Map<String, Object>> configs = new HashMap<>();
//        if (!cachedConfigs.isEmpty()) {
//            cachedConfigs.forEach((k, v) -> {
//                configs.put(String.valueOf(k), BeanUtils.objectToMap(v));
//            });
//        }
//        return configs;
//    }
//
//    @Override
//    public Map<String, Map<String, Object>> getFrontLoadConfigsOfTenant(Long tenantId) {
//        Map<String, Map<String, Object>> systemConfigs = self.getSystemConfigs();
//        Map<String, Map<String, Object>> tenantConfigs = self.getTenantConfigs(tenantId);
//        Map<String, Map<String, Object>> frontLoadConfigs = new HashMap<>();
//        log.info("筛选出加载到前端的系统配置");
//        systemConfigs.forEach((k, v) -> {
//            if (Boolean.parseBoolean(String.valueOf(v.get("frontLoad")))) {
//                frontLoadConfigs.put(k, v);
//            }
//        });
//        log.info("筛选出加载到前端的租户配置，并覆盖系统配置（如有）");
//        tenantConfigs.forEach((k, v) -> {
//            if (Boolean.parseBoolean(String.valueOf(v.get("frontLoad")))) {
//                frontLoadConfigs.put(k, v);
//            }
//        });
//        return frontLoadConfigs;
//    }
}
