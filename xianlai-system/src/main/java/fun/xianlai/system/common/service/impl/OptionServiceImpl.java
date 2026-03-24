package fun.xianlai.system.common.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.core.annotation.SimpleServiceLog;
import fun.xianlai.core.utils.ChecksumUtils;
import fun.xianlai.system.common.model.consts.ConstOptionCache;
import fun.xianlai.system.common.model.entity.Option;
import fun.xianlai.system.common.model.entity.OptionDefault;
import fun.xianlai.system.common.model.enums.EnumOptionScope;
import fun.xianlai.system.common.repository.OptionDefaultRepository;
import fun.xianlai.system.common.repository.OptionRepository;
import fun.xianlai.system.common.service.OptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class OptionServiceImpl implements OptionService {
    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private OptionDefaultRepository optionDefaultRepository;
    @Autowired
    private OptionRepository optionRepository;
    @Lazy
    @Autowired
    private OptionService self;

    @Override
    @SimpleServiceLog("更新前端加载的【系统参数】缓存")
    @Transactional
    public void updateFrontLoadSystemOptionsCache() {
        Map<String, Map<String, String>> mapOptions = new HashMap<>();
        // 先查询默认参数
        List<OptionDefault> optionDefaults = optionDefaultRepository.findByScopeAndFrontLoad(EnumOptionScope.SYSTEM, true);
        if (optionDefaults != null) {
            for (OptionDefault optionDefault : optionDefaults) {
                Map<String, String> valueObject = new HashMap<>();
                valueObject.put("value", optionDefault.getDefaultValue());
                valueObject.put("type", optionDefault.getValueType());
                mapOptions.put(optionDefault.getOptionKey(), valueObject);
            }
        }
        // 再查询参数实例，如有实例则覆盖默认参数
        List<Option> options = optionRepository.findByScopeAndFrontLoad(EnumOptionScope.SYSTEM, true);
        if (options != null) {
            for (Option option : options) {
                Map<String, String> valueObject = new HashMap<>();
                valueObject.put("value", option.getOptionValue());
                valueObject.put("type", option.getValueType());
                mapOptions.put(option.getOptionKey(), valueObject);
            }
        }
        // 缓存
        redis.opsForValue().set(ConstOptionCache.SYSTEM_OPTION_CHECKSUM_CACHE_KEY, ChecksumUtils.sha256Checksum(JSONObject.toJSONString(mapOptions)), Duration.ofHours(ConstOptionCache.SYSTEM_OPTION_CACHE_HOURS));
        redis.opsForValue().set(ConstOptionCache.SYSTEM_OPTION_CACHE_KEY, mapOptions, Duration.ofHours(ConstOptionCache.SYSTEM_OPTION_CACHE_HOURS));
    }

    @Override
    @SimpleServiceLog("更新前端加载的【租户参数】缓存")
    @Transactional
    public void updateFrontLoadTenantOptionsCache(Long tenantId) {
        Map<String, Map<String, String>> mapOptions = new HashMap<>();
        // 先查询默认参数
        List<OptionDefault> optionDefaults = optionDefaultRepository.findByScopeAndFrontLoad(EnumOptionScope.TENANT, true);
        if (optionDefaults != null) {
            for (OptionDefault optionDefault : optionDefaults) {
                Map<String, String> valueObject = new HashMap<>();
                valueObject.put("value", optionDefault.getDefaultValue());
                valueObject.put("type", optionDefault.getValueType());
                mapOptions.put(MessageFormat.format(optionDefault.getOptionKey(), tenantId), valueObject);
            }
        }
        // 再查询参数实例，如有实例则覆盖默认参数
        List<Option> options = optionRepository.findByScopeAndScopeIdAndFrontLoad(EnumOptionScope.TENANT, tenantId, true);
        if (options != null) {
            for (Option option : options) {
                Map<String, String> valueObject = new HashMap<>();
                valueObject.put("value", option.getOptionValue());
                valueObject.put("type", option.getValueType());
                mapOptions.put(option.getOptionKey(), valueObject);
            }
        }
        // 缓存
        redis.opsForValue().set(ConstOptionCache.TENANT_OPTION_CHECKSUM_CACHE_KEY_PREFIX + tenantId, ChecksumUtils.sha256Checksum(JSONObject.toJSONString(mapOptions)), Duration.ofHours(ConstOptionCache.TENANT_OPTION_CACHE_HOURS));
        redis.opsForValue().set(ConstOptionCache.TENANT_OPTION_CACHE_KEY_PREFIX + tenantId, mapOptions, Duration.ofHours(ConstOptionCache.TENANT_OPTION_CACHE_HOURS));
    }

    @Override
    @SimpleServiceLog("更新前端加载的【用户参数】缓存")
    @Transactional
    public void updateFrontLoadUserOptionsCache(Long userId) {
        Map<String, Map<String, String>> mapOptions = new HashMap<>();
        // 先查询默认参数
        List<OptionDefault> optionDefaults = optionDefaultRepository.findByScopeAndFrontLoad(EnumOptionScope.USER, true);
        if (optionDefaults != null) {
            for (OptionDefault optionDefault : optionDefaults) {
                Map<String, String> valueObject = new HashMap<>();
                valueObject.put("value", optionDefault.getDefaultValue());
                valueObject.put("type", optionDefault.getValueType());
                mapOptions.put(MessageFormat.format(optionDefault.getOptionKey(), userId), valueObject);
            }
        }
        // 再查询参数实例，如有实例则覆盖默认参数
        List<Option> options = optionRepository.findByScopeAndScopeIdAndFrontLoad(EnumOptionScope.USER, userId, true);
        if (options != null) {
            for (Option option : options) {
                Map<String, String> valueObject = new HashMap<>();
                valueObject.put("value", option.getOptionValue());
                valueObject.put("type", option.getValueType());
                mapOptions.put(option.getOptionKey(), valueObject);
            }
        }
        // 缓存
        redis.opsForValue().set(ConstOptionCache.USER_OPTION_CHECKSUM_CACHE_KEY_PREFIX + userId, ChecksumUtils.sha256Checksum(JSONObject.toJSONString(mapOptions)), Duration.ofHours(ConstOptionCache.USER_OPTION_CACHE_HOURS));
        redis.opsForValue().set(ConstOptionCache.USER_OPTION_CACHE_KEY_PREFIX + userId, mapOptions, Duration.ofHours(ConstOptionCache.USER_OPTION_CACHE_HOURS));
    }

    @Override
    @SimpleServiceLog("从缓存获取前端加载的【系统参数】参数")
    public Map<String, Map<String, String>> getFrontLoadSystemOptionsFromCache() {
        Map<String, Map<String, String>> options = (Map<String, Map<String, String>>) redis.opsForValue().get(ConstOptionCache.SYSTEM_OPTION_CACHE_KEY);
        if (options == null) {
            self.updateFrontLoadSystemOptionsCache();
            options = (Map<String, Map<String, String>>) redis.opsForValue().get(ConstOptionCache.SYSTEM_OPTION_CACHE_KEY);
        }
        return options;
    }

    @Override
    @SimpleServiceLog("从缓存获取前端加载的【租户参数】参数")
    public Map<String, Map<String, String>> getFrontLoadTenantOptionsFromCache(Long tenantId) {
        Map<String, Map<String, String>> options = (Map<String, Map<String, String>>) redis.opsForValue().get(ConstOptionCache.TENANT_OPTION_CACHE_KEY_PREFIX + tenantId);
        if (options == null) {
            self.updateFrontLoadTenantOptionsCache(tenantId);
            options = (Map<String, Map<String, String>>) redis.opsForValue().get(ConstOptionCache.TENANT_OPTION_CACHE_KEY_PREFIX + tenantId);
        }
        return options;
    }

    @Override
    @SimpleServiceLog("从缓存获取前端加载的【用户参数】参数")
    public Map<String, Map<String, String>> getFrontLoadUserOptionsFromCache(Long userId) {
        Map<String, Map<String, String>> options = (Map<String, Map<String, String>>) redis.opsForValue().get(ConstOptionCache.USER_OPTION_CACHE_KEY_PREFIX + userId);
        if (options == null) {
            self.updateFrontLoadUserOptionsCache(userId);
            options = (Map<String, Map<String, String>>) redis.opsForValue().get(ConstOptionCache.USER_OPTION_CACHE_KEY_PREFIX + userId);
        }
        return options;
    }

    @Override
    @SimpleServiceLog("更新后端加载的【系统参数】缓存")
    @Transactional
    public void updateBackLoadSystemOptionsCache() {
        List<OptionDefault> optionDefaults = optionDefaultRepository.findByScope(EnumOptionScope.SYSTEM);
        for (OptionDefault optionDefault : optionDefaults) {
            Map<String, String> valueObject = new HashMap<>();
            valueObject.put("value", optionDefault.getDefaultValue());
            valueObject.put("type", optionDefault.getValueType());
            redis.opsForValue().set(ConstOptionCache.SINGLE_OPTION_CACHE_KEY_PREFIX + optionDefault.getOptionKey(), valueObject, Duration.ofHours(ConstOptionCache.SYSTEM_OPTION_CACHE_HOURS));
        }
        List<Option> options = optionRepository.findByScope(EnumOptionScope.SYSTEM);
        for (Option option : options) {
            Map<String, String> valueObject = new HashMap<>();
            valueObject.put("value", option.getOptionValue());
            valueObject.put("type", option.getValueType());
            redis.opsForValue().set(ConstOptionCache.SINGLE_OPTION_CACHE_KEY_PREFIX + option.getOptionKey(), valueObject, Duration.ofHours(ConstOptionCache.SYSTEM_OPTION_CACHE_HOURS));
        }
    }

    @Override
    @SimpleServiceLog("更新后端加载的【租户参数】缓存")
    @Transactional
    public void updateBackLoadTenantOptionsCache(Long tenantId) {
        List<OptionDefault> optionDefaults = optionDefaultRepository.findByScope(EnumOptionScope.TENANT);
        for (OptionDefault optionDefault : optionDefaults) {
            Map<String, String> valueObject = new HashMap<>();
            valueObject.put("value", optionDefault.getDefaultValue());
            valueObject.put("type", optionDefault.getValueType());
            redis.opsForValue().set(ConstOptionCache.SINGLE_OPTION_CACHE_KEY_PREFIX + MessageFormat.format(optionDefault.getOptionKey(), tenantId), valueObject, Duration.ofHours(ConstOptionCache.TENANT_OPTION_CACHE_HOURS));
        }
        List<Option> options = optionRepository.findByScopeAndScopeId(EnumOptionScope.TENANT, tenantId);
        for (Option option : options) {
            Map<String, String> valueObject = new HashMap<>();
            valueObject.put("value", option.getOptionValue());
            valueObject.put("type", option.getValueType());
            redis.opsForValue().set(ConstOptionCache.SINGLE_OPTION_CACHE_KEY_PREFIX + option.getOptionKey(), valueObject, Duration.ofHours(ConstOptionCache.TENANT_OPTION_CACHE_HOURS));
        }
    }

    @Override
    @SimpleServiceLog("更新后端加载的【用户参数】缓存")
    @Transactional
    public void updateBackLoadUserOptionsCache(Long userId) {
        List<OptionDefault> optionDefaults = optionDefaultRepository.findByScope(EnumOptionScope.USER);
        for (OptionDefault optionDefault : optionDefaults) {
            Map<String, String> valueObject = new HashMap<>();
            valueObject.put("value", optionDefault.getDefaultValue());
            valueObject.put("type", optionDefault.getValueType());
            redis.opsForValue().set(ConstOptionCache.SINGLE_OPTION_CACHE_KEY_PREFIX + MessageFormat.format(optionDefault.getOptionKey(), userId), valueObject, Duration.ofHours(ConstOptionCache.USER_OPTION_CACHE_HOURS));
        }
        List<Option> options = optionRepository.findByScopeAndScopeId(EnumOptionScope.USER, userId);
        for (Option option : options) {
            Map<String, String> valueObject = new HashMap<>();
            valueObject.put("value", option.getOptionValue());
            valueObject.put("type", option.getValueType());
            redis.opsForValue().set(ConstOptionCache.SINGLE_OPTION_CACHE_KEY_PREFIX + option.getOptionKey(), valueObject, Duration.ofHours(ConstOptionCache.USER_OPTION_CACHE_HOURS));
        }
    }

    @Override
    @SimpleServiceLog("更新某个后端加载参数的缓存")
    @Transactional
    public void updateCertainBackLoadOptionCache(String key) {
        redis.delete(ConstOptionCache.SINGLE_OPTION_CACHE_KEY_PREFIX + key);
        String defaultKey = "";
        long cacheHours = 0;
        int firstDot = key.indexOf('.');
        int secondDot = key.indexOf('.', firstDot + 1);
        String firstItem = key.substring(0, firstDot).toUpperCase();
        switch (firstItem) {
            case EnumOptionScope.SYSTEM -> {
                defaultKey = key;
                cacheHours = ConstOptionCache.SYSTEM_OPTION_CACHE_HOURS;
            }
            case EnumOptionScope.TENANT -> {
                defaultKey = firstItem + ".{0}." + key.substring(secondDot + 1);
                cacheHours = ConstOptionCache.TENANT_OPTION_CACHE_HOURS;
            }
            case EnumOptionScope.USER -> {
                defaultKey = firstItem + ".{0}." + key.substring(secondDot + 1);
                cacheHours = ConstOptionCache.USER_OPTION_CACHE_HOURS;
            }
        }
        Optional<OptionDefault> optionDefault = optionDefaultRepository.findByOptionKey(defaultKey);
        if (optionDefault.isPresent()) {
            Map<String, String> valueObject = new HashMap<>();
            valueObject.put("value", optionDefault.get().getDefaultValue());
            valueObject.put("type", optionDefault.get().getValueType());
            redis.opsForValue().set(ConstOptionCache.SINGLE_OPTION_CACHE_KEY_PREFIX + key, valueObject, Duration.ofHours(cacheHours));
        }
        Optional<Option> option = optionRepository.findByOptionKey(key);
        if (option.isPresent()) {
            Map<String, String> valueObject = new HashMap<>();
            valueObject.put("value", option.get().getOptionValue());
            valueObject.put("type", option.get().getValueType());
            redis.opsForValue().set(ConstOptionCache.SINGLE_OPTION_CACHE_KEY_PREFIX + key, valueObject, Duration.ofHours(cacheHours));
        }
    }

    @Override
    @SimpleServiceLog("从缓存获取某个后端加载的参数")
    public Map<String, String> getCertainBackLoadOptionFromCache(String key) {
        Map<String, String> value = (Map<String, String>) redis.opsForValue().get(ConstOptionCache.SINGLE_OPTION_CACHE_KEY_PREFIX + key);
        if (value == null) {
            self.updateCertainBackLoadOptionCache(key);
            value = (Map<String, String>) redis.opsForValue().get(ConstOptionCache.SINGLE_OPTION_CACHE_KEY_PREFIX + key);
        }
        return value;
    }

//    @Override
//    @SimpleServiceLog("修改参数")
//    @Transactional
//    public DataMap edit(SysOptionDefault option) {
//        Optional<SysOptionDefault> oldOption = optionRepository.findById(option.getId());
//        if (oldOption.isPresent()) {
//            SysOptionDefault newOption = oldOption.get();
//            BeanUtils.copyPropertiesNotNull(option, newOption);
//            try {
//                newOption = optionRepository.save(newOption);
//            } catch (DataIntegrityViolationException e) {
//                log.info(e.getMessage());
//                throw new SysException("参数Key已存在");
//            }
//            if (!EnumOptionScope.TENANT_TEMP.equals(newOption.getType()) && !EnumOptionScope.USER_TEMP.equals(newOption.getType())) {
//                if (newOption.getFrontLoad()) {
//                    switch (newOption.getType()) {
//                        case EnumOptionScope.SYSTEM -> {
//                            self.updateFrontLoadSystemOptionsCache();
//                        }
//                        case EnumOptionScope.TENANT -> {
//                            self.updateFrontLoadTenantOptionsCache(newOption.getIdentifier());
//                        }
//                        case EnumOptionScope.USER -> {
//                            self.updateFrontLoadUserOptionsCache(newOption.getIdentifier());
//                        }
//                    }
//                }
//                self.updateCertainBackLoadOptionCache(newOption.getOptionKey());
//            }
//            return new DataMap("option", newOption);
//        } else {
//            throw new SysException("要修改的参数不存在");
//        }
//    }

//    @Override
//    @SimpleServiceLog("以String类型读取参数值")
//    public Optional<String> readValueInString(String key) {
//        String value = self.getCertainBackLoadOptionValueFromCache(key);
//        return value != null ? value.describeConstable() : Optional.empty();
//    }
//
//    @Override
//    @SimpleServiceLog("以Integer类型读取参数值")
//    public Optional<Integer> readValueInInteger(String key) {
//        String value = self.getCertainBackLoadOptionValueFromCache(key);
//        return value != null ? ((Integer) Integer.parseInt(value)).describeConstable() : Optional.empty();
//    }
//
//    @Override
//    @SimpleServiceLog("以Long类型读取参数值")
//    public Optional<Long> readValueInLong(String key) {
//        String value = self.getCertainBackLoadOptionValueFromCache(key);
//        return value != null ? ((Long) Long.parseLong(value)).describeConstable() : Optional.empty();
//    }
}
