package fun.xianlai.app.common.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.app.common.model.entity.SysOption;
import fun.xianlai.app.common.repository.SysOptionRepository;
import fun.xianlai.app.common.service.OptionService;
import fun.xianlai.core.annotation.SimpleServiceLog;
import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.response.DataMap;
import fun.xianlai.core.utils.ChecksumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Service
public class OptionServiceImpl implements OptionService {
    private static final long CACHE_HOURS = 720L;   // 30天
    private static final String CACHE_PREFIX = "backOption:";

    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private SysOptionRepository sysOptionRepository;
    @Lazy
    @Autowired
    private OptionService self;

    @Override
    @SimpleServiceLog("缓存加载到前端的参数")
    @Transactional
    public void cacheFrontLoadOptions() {
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
        redis.opsForValue().set("optionsChecksum", ChecksumUtil.sha256Checksum(JSONObject.toJSONString(mapOptions)), Duration.ofHours(CACHE_HOURS));
        redis.opsForValue().set("options", mapOptions, Duration.ofHours(CACHE_HOURS));
    }

    @Override
    @SimpleServiceLog("从缓存获取加载到前端的参数")
    public Map<String, Map<String, String>> getFrontLoadOptionsFromCache() {
        Map<String, Map<String, String>> options = (Map<String, Map<String, String>>) redis.opsForValue().get("options");
        if (options == null) {
            self.cacheFrontLoadOptions();
            options = (Map<String, Map<String, String>>) redis.opsForValue().get("options");
        }
        return options;
    }

    @Override
    @SimpleServiceLog("缓存加载到后端的参数")
    public void cacheBackLoadOptions() {
        List<SysOption> options = sysOptionRepository.findByBackLoad(true);
        if (options != null) {
            for (SysOption option : options) {
                redis.opsForValue().set(CACHE_PREFIX + option.getOptionKey(), option.getOptionValue(), Duration.ofHours(CACHE_HOURS));
            }
        }
    }

    @Override
    @SimpleServiceLog("缓存某个加载到后端的参数")
    public void cacheCertainBackLoadOption(String key) {
        Optional<SysOption> option = sysOptionRepository.findByOptionKeyAndBackLoad(key, true);
        redis.delete(CACHE_PREFIX + key);
        if (option.isPresent()) {
            redis.opsForValue().set(CACHE_PREFIX + key, option.get().getOptionValue(), Duration.ofHours(CACHE_HOURS));
        }
    }

    @Override
    @SimpleServiceLog("从缓存获取某个加载到后端的参数值")
    public String getCertainBackLoadOptionValueFromCache(String key) {
        String value = (String) redis.opsForValue().get(CACHE_PREFIX + key);
        if (value == null) {
            self.cacheCertainBackLoadOption(key);
            value = (String) redis.opsForValue().get(CACHE_PREFIX + key);
        }
        return value;
    }

    @Override
    @SimpleServiceLog("新增参数")
    @Transactional
    public DataMap add(SysOption option) {
        try {
            option.setId(null);
            SysOption savedOption = sysOptionRepository.save(option);
            if (savedOption.getFrontLoad()) self.cacheFrontLoadOptions();
            if (savedOption.getBackLoad()) self.cacheBackLoadOptions();
            DataMap result = new DataMap();
            result.put("option", savedOption);
            return result;
        } catch (Exception e) {
            throw new SysException("参数Key已存在");
        }
    }

    @Override
    @SimpleServiceLog("删除参数")
    @Transactional
    public void delete(Long optionId) {
        sysOptionRepository.deleteById(optionId);
        self.cacheFrontLoadOptions();
        self.cacheBackLoadOptions();
    }

    @Override
    @SimpleServiceLog("获取分类后的参数列表")
    public DataMap getClassifiedList() {
        Sort sort = Sort.by(Sort.Order.asc("category"), Sort.Order.asc("sortId"));
        List<SysOption> options = sysOptionRepository.findAll(sort);
        Map<String, List<SysOption>> mapOptions = new HashMap<>();
        for (SysOption option : options) {
            if (!mapOptions.containsKey(option.getCategory())) {
                mapOptions.put(option.getCategory(), new ArrayList<>());
            }
            mapOptions.get(option.getCategory()).add(option);
        }
        return new DataMap("options", mapOptions);
    }

    @Override
    @SimpleServiceLog("以String类型读取参数值")
    public Optional<String> readValueInString(String key) {
        String value = self.getCertainBackLoadOptionValueFromCache(key);
        return value != null ? value.describeConstable() : Optional.empty();
    }

    @Override
    @SimpleServiceLog("以Integer类型读取参数值")
    public Optional<Integer> readValueInInteger(String key) {
        String value = self.getCertainBackLoadOptionValueFromCache(key);
        return value != null ? ((Integer) Integer.parseInt(value)).describeConstable() : Optional.empty();
    }

    @Override
    @SimpleServiceLog("以Long类型读取参数值")
    public Optional<Long> readValueInLong(String key) {
        String value = self.getCertainBackLoadOptionValueFromCache(key);
        return value != null ? ((Long) Long.parseLong(value)).describeConstable() : Optional.empty();
    }
}
