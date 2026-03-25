package fun.xianlai.system.common.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.system.common.model.consts.ConstApiCache;
import fun.xianlai.system.common.model.entity.XLApi;
import fun.xianlai.system.common.repository.XLApiRepository;
import fun.xianlai.system.common.service.ApiService;
import fun.xianlai.common.annotation.SimpleServiceLog;
import fun.xianlai.common.utils.ChecksumUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class ApiServiceImpl implements ApiService {
    @Autowired
    private RedisTemplate<String, Object> redis;
    @Lazy
    @Autowired
    private ApiService self;
    @Autowired
    private XLApiRepository apiRepository;

    @Override
    @SimpleServiceLog("更新接口缓存")
    @Transactional
    public void updateApisCache() {
        List<XLApi> apis = apiRepository.findAll();
        redis.opsForValue().set(ConstApiCache.API_CHECKSUM_CACHE_KEY, ChecksumUtils.sha256Checksum(JSONObject.toJSONString(apis)), Duration.ofHours(ConstApiCache.API_CACHE_HOURS));
        redis.opsForValue().set(ConstApiCache.API_CACHE_KEY, apis, Duration.ofHours(ConstApiCache.API_CACHE_HOURS));
    }

    @Override
    @SimpleServiceLog("从缓存获取接口")
    public List<XLApi> getApisFromCache() {
        List<XLApi> apis = (List<XLApi>) redis.opsForValue().get(ConstApiCache.API_CACHE_KEY);
        if (apis == null) {
            self.updateApisCache();
            apis = (List<XLApi>) redis.opsForValue().get(ConstApiCache.API_CACHE_KEY);
        }
        return apis;
    }

//    @Override
//    @ServiceLog("新增接口")
//    @Transactional
//    public DataMap add(XLApi api) {
//        try {
//            api.setId(null);
//            XLApi savedApi = spiRepository.save(api);
//            Long rowNum = spiRepository.findRowNumById(savedApi.getId());
//            self.cacheApis();
//            DataMap result = new DataMap();
//            result.put("api", savedApi);
//            result.put("rowNum", rowNum);
//            return result;
//        } catch (DataIntegrityViolationException e) {
//            throw new SysException("接口已存在");
//        }
//    }
//
//    @Override
//    @ServiceLog("删除接口")
//    @Transactional
//    public void delete(Long apiId) {
//        spiRepository.deleteById(apiId);
//        self.cacheApis();
//    }
//
//    @Override
//    @ServiceLog("修改接口")
//    @Transactional
//    public DataMap edit(XLApi api) {
//        Optional<XLApi> oldApi = spiRepository.findById(api.getId());
//        if (oldApi.isPresent()) {
//            XLApi newApi = oldApi.get();
//            BeanUtils.copyPropertiesNotNull(api, newApi);
//            try {
//                newApi = spiRepository.save(newApi);
//            } catch (DataIntegrityViolationException e) {
//                log.info(e.getMessage());
//                throw new SysException("接口调用路径已存在");
//            }
//            self.cacheApis();
//            return new DataMap("api", newApi);
//        } else {
//            throw new SysException("要修改的接口不存在");
//        }
//    }
//
//    @Override
//    @ServiceLog("条件查询接口分页")
//    public Page<XLApi> getPageConditionally(int pageNum, int pageSize, XLApi condition) {
//        String callPath = BeanUtils.getFieldValue(condition, "callPath", String.class);
//        String description = BeanUtils.getFieldValue(condition, "description", String.class);
//        RequestMethod requestMethod = BeanUtils.getFieldValue(condition, "requestMethod", RequestMethod.class);
//        String url = BeanUtils.getFieldValue(condition, "url", String.class);
//
//        Sort sort = Sort.by(Sort.Order.asc("callPath"));
//        if (pageNum >= 0 && pageSize > 0) {
//            log.info("分页查询");
//            Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
//            return spiRepository.findConditionally(callPath, description, requestMethod, url, pageable);
//        } else {
//            log.info("全表查询");
//            return spiRepository.findConditionally(callPath, description, requestMethod, url, Pageable.unpaged(sort));
//        }
//    }
}
