package fun.xianlai.system.core.service.impl;

import fun.xianlai.common.starter.service.ConfigService;
import fun.xianlai.system.core.service.ConfigManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class ConfigManageServiceImpl implements ConfigManageService {
    @Autowired
    private ConfigService configService;

    @Override
    public Map<String, Map<String, Object>> getTenantFrontLoadConfigs(Long tenantId) {
        Map<String, Map<String, Object>> allConfigs = configService.getTenantConfigsFromCache(tenantId);
        Map<String, Map<String, Object>> frontLoadConfigs = new HashMap<>();
        allConfigs.forEach((k, v) -> {
            if (Boolean.TRUE.equals(v.remove("frontLoad"))) {
                frontLoadConfigs.put(k, v);
            }
        });
        return frontLoadConfigs;
    }

//    @Override
//    public <T> T getConfig(Long tenantId, String key, Class<T> clazz) {
//        String keyAll = MessageFormat.format(TenantConst.CONFIG_ALL_CACHE_KEY, tenantId);
//        if (!redis.hasKey(keyAll)) {
//            this.cacheTenantConfigs(tenantId);
//        } else {
//            if (redis.hasKey(ConfigConst.CONFIG_CACHE_KEY)) {
//                String SCUTString = (String) BeanUtils.objectToMap(redis.opsForHash().get(ConfigConst.CONFIG_CACHE_KEY, ConfigConst.CONFIG_UPDATE_TIME_CONFIG_KEY)).get("value");
//                String SCUTStringInTenantCache = (String) BeanUtils.objectToMap(redis.opsForHash().get(keyAll, ConfigConst.CONFIG_UPDATE_TIME_CONFIG_KEY)).get("value");
//                if (!SCUTStringInTenantCache.equals(SCUTString)) {
//                    this.cacheTenantConfigs(tenantId);
//                }
//            }
//        }
//
//        String value = (String) BeanUtils.objectToMap(redis.opsForHash().get(keyAll, key)).get("value");
//        if (value == null) return null;
//        try {
//            if (clazz == String.class) return clazz.cast(value);
//            if (clazz == Integer.class) return clazz.cast(Integer.valueOf(value.trim()));
//            if (clazz == Long.class) return clazz.cast(Long.valueOf(value.trim()));
//            if (clazz == Boolean.class) return clazz.cast(Boolean.valueOf(value.trim()));
//            if (clazz == Double.class) return clazz.cast(Double.valueOf(value.trim()));
//            throw new BizException(RetCode.NOT_SUPPORT, "不支持的数据类型: " + clazz.getSimpleName());
//        } catch (ClassCastException e) {
//            throw new BizException(RetCode.DATA_FORMAT_ERROR, "数据格式错误，无法转换为" + clazz.getSimpleName());
//        }
//    }
//
//    @Override
//    public <T> T getConfigOrDefault(Long tenantId, String key, Class<T> clazz, T defaultValue) {
//        T value = this.getConfig(tenantId, key, clazz);
//        return value == null ? defaultValue : value;
//    }
}
