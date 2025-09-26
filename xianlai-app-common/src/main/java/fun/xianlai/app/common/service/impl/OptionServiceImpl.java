package fun.xianlai.app.common.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.basic.annotation.SimpleServiceLog;
import fun.xianlai.basic.utils.ChecksumUtil;
import fun.xianlai.app.common.model.entity.SysOption;
import fun.xianlai.app.common.model.enums.JavaType;
import fun.xianlai.app.common.repository.SysOptionRepository;
import fun.xianlai.app.common.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Service
public class OptionServiceImpl implements OptionService {
    private static final long CACHE_HOURS = 168L;   // 7天
    private static final String CACHE_PREFIX = "backSysOption:";

    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private SysOptionRepository sysOptionRepository;
    @Lazy
    @Autowired
    private OptionService self;

    @Override
    @SimpleServiceLog("缓存加载到前端的系统参数")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void cacheFrontLoadSysOptions() {
        List<SysOption> options = sysOptionRepository.findByFrontLoad(true);
        Map<String, Map<String, String>> mapOptions = new HashMap<>();
        if (options != null) {
            for (SysOption option : options) {
                Map<String, String> valueObject = new HashMap<>();
                valueObject.put("value", option.getOptionValue());
                valueObject.put("type", option.getJsType().toString());
                mapOptions.put(option.getOptionKey(), valueObject);
            }
        }
        redis.opsForValue().set("sysOptionsChecksum", ChecksumUtil.sha256Checksum(JSONObject.toJSONString(mapOptions)), Duration.ofHours(CACHE_HOURS));
        redis.opsForValue().set("sysOptions", mapOptions, Duration.ofHours(CACHE_HOURS));
    }

    @Override
    @SimpleServiceLog("从缓存获取加载到前端的系统参数")
    public Map<String, Map<String, String>> getFrontLoadSysOptionsFromCache() {
        Map<String, Map<String, String>> options = (Map<String, Map<String, String>>) redis.opsForValue().get("sysOptions");
        if (options == null) {
            self.cacheFrontLoadSysOptions();
            options = (Map<String, Map<String, String>>) redis.opsForValue().get("sysOptions");
        }
        return options;
    }

    @Override
    @SimpleServiceLog("从缓存获取加载到前端的系统参数的checksum")
    public String getFrontLoadSysOptionsChecksumFromCache() {
        return (String) redis.opsForValue().get("sysOptionsChecksum");
    }

    @Override
    @SimpleServiceLog("缓存加载到后端的系统参数缓存")
    public void cacheBackLoadSysOptions() {
        List<SysOption> options = sysOptionRepository.findByBackLoad(true);
        if (options != null) {
            for (SysOption option : options) {
                Map<String, Object> valueObject = new HashMap<>();
                valueObject.put("value", option.getOptionValue());
                valueObject.put("type", option.getJavaType());
                redis.opsForValue().set(CACHE_PREFIX + option.getOptionKey(), valueObject, Duration.ofHours(CACHE_HOURS));
            }
        }
    }

    @Override
    @SimpleServiceLog("缓存某个加载到后端的系统参数")
    public void cacheCertainBackLoadSysOption(String key) {
        Optional<SysOption> option = sysOptionRepository.findByOptionKeyAndBackLoad(key, true);
        redis.delete(CACHE_PREFIX + key);
        if (option.isPresent()) {
            Map<String, Object> valueObject = new HashMap<>();
            valueObject.put("value", option.get().getOptionValue());
            valueObject.put("type", option.get().getJavaType());
            redis.opsForValue().set(CACHE_PREFIX + key, valueObject, Duration.ofHours(CACHE_HOURS));
        }
    }

    @Override
    @SimpleServiceLog("从缓存获取某个加载到后端的系统参数值")
    public Optional<Object> getCertainBackLoadSysOptionValueFromCache(String key) {
        Map<String, Object> valueObject = (HashMap<String, Object>) redis.opsForValue().get(CACHE_PREFIX + key);
        if (valueObject == null) {
            self.cacheCertainBackLoadSysOption(key);
        }
        // null时再次获取后才做的类型转换
        if (valueObject != null) {
            String value = (String) valueObject.get("value");
            JavaType type = (JavaType) valueObject.get("type");
            if (JavaType.BYTE.equals(type)) {
                return Optional.of(Byte.parseByte(value));
            }
            if (JavaType.SHORT.equals(type)) {
                return Optional.of(Short.parseShort(value));
            }
            if (JavaType.INT.equals(type)) {
                return Optional.of(Integer.parseInt(value));
            }
            if (JavaType.LONG.equals(type)) {
                return Optional.of(Long.parseLong(value));
            }
            if (JavaType.FLOAT.equals(type)) {
                return Optional.of(Float.parseFloat(value));
            }
            if (JavaType.DOUBLE.equals(type)) {
                return Optional.of(Double.parseDouble(value));
            }
            if (JavaType.BOOLEAN.equals(type)) {
                return Optional.of(Boolean.parseBoolean(value));
            }
            if (JavaType.CHAR.equals(type)) {
                return Optional.of(value.charAt(0));
            }
            if (JavaType.STRING.equals(type)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
