package fun.xianlai.system.common.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.core.annotation.SimpleServiceLog;
import fun.xianlai.core.utils.ChecksumUtils;
import fun.xianlai.system.common.model.consts.ConstOptionCache;
import fun.xianlai.system.common.model.entity.SysOption;
import fun.xianlai.system.common.model.enums.EnumOptionType;
import fun.xianlai.system.common.repository.OptionRepository;
import fun.xianlai.system.common.service.OptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private OptionRepository optionRepository;
    @Lazy
    @Autowired
    private OptionService self;

    @Override
    @SimpleServiceLog("更新前端加载的【系统参数】缓存")
    @Transactional
    public void updateFrontLoadSystemOptionsCache() {
        List<SysOption> options = optionRepository.findByTypeAndFrontLoad(EnumOptionType.SYSTEM, true);
        Map<String, Map<String, String>> mapOptions = new HashMap<>();
        if (options != null) {
            for (SysOption option : options) {
                Map<String, String> valueObject = new HashMap<>();
                valueObject.put("value", option.getOptionValue());
                valueObject.put("type", option.getValueType());
                mapOptions.put(option.getOptionKey(), valueObject);
            }
        }
        redis.opsForValue().set(ConstOptionCache.SYSTEM_OPTION_CHECKSUM_CACHE_KEY, ChecksumUtils.sha256Checksum(JSONObject.toJSONString(mapOptions)), Duration.ofHours(ConstOptionCache.SYSTEM_OPTION_CACHE_HOURS));
        redis.opsForValue().set(ConstOptionCache.SYSTEM_OPTION_CACHE_KEY, mapOptions, Duration.ofHours(ConstOptionCache.SYSTEM_OPTION_CACHE_HOURS));
    }

    @Override
    @SimpleServiceLog("更新前端加载的【租户参数】缓存")
    @Transactional
    public void updateFrontLoadTenantOptionsCache(Long tenantId) {
        List<SysOption> options = optionRepository.findByTypeAndIdentifierAndFrontLoad(EnumOptionType.TENANT, tenantId, true);
        Map<String, Map<String, String>> mapOptions = new HashMap<>();
        if (options != null) {
            for (SysOption option : options) {
                Map<String, String> valueObject = new HashMap<>();
                valueObject.put("value", option.getOptionValue());
                valueObject.put("type", option.getValueType());
                mapOptions.put(option.getOptionKey(), valueObject);
            }
        }
        redis.opsForValue().set(ConstOptionCache.TENANT_OPTION_CHECKSUM_CACHE_KEY_PREFIX + tenantId, ChecksumUtils.sha256Checksum(JSONObject.toJSONString(mapOptions)), Duration.ofHours(ConstOptionCache.TENANT_OPTION_CACHE_HOURS));
        redis.opsForValue().set(ConstOptionCache.TENANT_OPTION_CACHE_KEY_PREFIX + tenantId, mapOptions, Duration.ofHours(ConstOptionCache.TENANT_OPTION_CACHE_HOURS));
    }

    @Override
    @SimpleServiceLog("更新前端加载的【用户参数】缓存")
    @Transactional
    public void updateFrontLoadUserOptionsCache(Long userId) {
        List<SysOption> options = optionRepository.findByTypeAndIdentifierAndFrontLoad(EnumOptionType.USER, userId, true);
        Map<String, Map<String, String>> mapOptions = new HashMap<>();
        if (options != null) {
            for (SysOption option : options) {
                Map<String, String> valueObject = new HashMap<>();
                valueObject.put("value", option.getOptionValue());
                valueObject.put("type", option.getValueType());
                mapOptions.put(option.getOptionKey(), valueObject);
            }
        }
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
    @SimpleServiceLog("更新所有后端加载的【系统参数】缓存")
    @Transactional
    public void updateAllBackLoadSystemOptionsCache() {
        List<SysOption> options = optionRepository.findByType(EnumOptionType.SYSTEM);
        for (SysOption option : options) {
            Map<String, String> valueObject = new HashMap<>();
            valueObject.put("value", option.getOptionValue());
            valueObject.put("type", option.getValueType());
            redis.opsForValue().set(ConstOptionCache.SINGLE_OPTION_CACHE_KEY_PREFIX + option.getOptionKey(), valueObject, Duration.ofHours(ConstOptionCache.SYSTEM_OPTION_CACHE_HOURS));
        }
    }

    @Override
    @SimpleServiceLog("更新所有后端加载的【租户参数】缓存")
    @Transactional
    public void updateAllBackLoadTenantOptionsCache(Long tenantId) {
        List<SysOption> options = optionRepository.findByTypeAndIdentifier(EnumOptionType.TENANT, tenantId);
        for (SysOption option : options) {
            Map<String, String> valueObject = new HashMap<>();
            valueObject.put("value", option.getOptionValue());
            valueObject.put("type", option.getValueType());
            redis.opsForValue().set(ConstOptionCache.SINGLE_OPTION_CACHE_KEY_PREFIX + option.getOptionKey(), valueObject, Duration.ofHours(ConstOptionCache.TENANT_OPTION_CACHE_HOURS));
        }
    }

    @Override
    @SimpleServiceLog("更新所有后端加载的【用户参数】缓存")
    @Transactional
    public void updateAllBackLoadUserOptionsCache(Long userId) {
        List<SysOption> options = optionRepository.findByTypeAndIdentifier(EnumOptionType.USER, userId);
        for (SysOption option : options) {
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
        Optional<SysOption> option = optionRepository.findByOptionKey(key);
        if (option.isPresent()) {
            Map<String, String> valueObject = new HashMap<>();
            valueObject.put("value", option.get().getOptionValue());
            valueObject.put("type", option.get().getValueType());
            long cacheHours = 0;
            switch (option.get().getType()) {
                case EnumOptionType.SYSTEM:
                    cacheHours = ConstOptionCache.SYSTEM_OPTION_CACHE_HOURS;
                    break;
                case EnumOptionType.TENANT:
                    cacheHours = ConstOptionCache.TENANT_OPTION_CACHE_HOURS;
                    break;
                case EnumOptionType.USER:
                    cacheHours = ConstOptionCache.USER_OPTION_CACHE_HOURS;
                    break;
            }
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
//    @SimpleServiceLog("新增参数")
//    @Transactional
//    public DataMap add(SysOption option) {
//        try {
//            option.setId(null);
//            SysOption savedOption = optionRepository.save(option);
//            if (savedOption.getFrontLoad()) self.updateAllFrontLoadOptionsCache();
//            self.updateCertainBackLoadOptionCache(savedOption.getOptionKey());
//            DataMap result = new DataMap();
//            result.put("option", savedOption);
//            return result;
//        } catch (DataIntegrityViolationException e) {
//            throw new SysException("参数Key已存在");
//        } catch (Exception e) {
//            throw new SysException("其他异常: " + e.getMessage());
//        }
//    }
//
//    @Override
//    @SimpleServiceLog("删除参数")
//    @Transactional
//    public void delete(Long optionId) {
//        Optional<SysOption> option = optionRepository.findById(optionId);
//        if (option.isPresent()) {
//            optionRepository.deleteById(optionId);
//            if (option.get().getFrontLoad()) self.updateAllFrontLoadOptionsCache();
//            self.updateCertainBackLoadOptionCache(option.get().getOptionKey());
//        } else {
//            throw new SysException("要删除的参数不存在");
//        }
//    }
//
//    @Override
//    @SimpleServiceLog("修改参数")
//    @Transactional
//    public DataMap edit(SysOption option) {
//        Optional<SysOption> oldOption = optionRepository.findById(option.getId());
//        if (oldOption.isPresent()) {
//            SysOption newOption = oldOption.get();
//            BeanUtils.copyPropertiesNotNull(option, newOption);
//            try {
//                newOption = optionRepository.save(newOption);
//            } catch (DataIntegrityViolationException e) {
//                log.info(e.getMessage());
//                throw new SysException("参数Key已存在");
//            }
//            if (newOption.getFrontLoad()) self.updateAllFrontLoadOptionsCache();
//            self.updateCertainBackLoadOptionCache(newOption.getOptionKey());
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
