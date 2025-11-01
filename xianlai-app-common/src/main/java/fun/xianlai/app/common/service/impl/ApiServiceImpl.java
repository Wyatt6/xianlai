package fun.xianlai.app.common.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.app.common.model.entity.SysApi;
import fun.xianlai.app.common.model.form.SysApiCondition;
import fun.xianlai.app.common.repository.SysApiRepository;
import fun.xianlai.app.common.service.ApiService;
import fun.xianlai.core.annotation.ServiceLog;
import fun.xianlai.core.annotation.SimpleServiceLog;
import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.response.DataMap;
import fun.xianlai.core.utils.ChecksumUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class ApiServiceImpl implements ApiService {
    private static final long CACHE_HOURS = 720L;   // 30天

    @Autowired
    private RedisTemplate<String, Object> redis;
    @Lazy
    @Autowired
    private ApiService self;
    @Autowired
    private SysApiRepository sysApiRepository;

    @Override
    @SimpleServiceLog("缓存接口")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void cacheApis() {
        List<SysApi> apis = sysApiRepository.findAll();
        redis.opsForValue().set("apisChecksum", ChecksumUtil.sha256Checksum(JSONObject.toJSONString(apis)), Duration.ofHours(CACHE_HOURS));
        redis.opsForValue().set("apis", apis, Duration.ofHours(CACHE_HOURS));
    }

    @Override
    @SimpleServiceLog("从缓存获取接口")
    public List<SysApi> getApisFromCache() {
        List<SysApi> apis = (List<SysApi>) redis.opsForValue().get("apis");
        if (apis == null) {
            self.cacheApis();
            apis = (List<SysApi>) redis.opsForValue().get("apis");
        }
        return apis;
    }

    @Override
    @SimpleServiceLog("新增接口")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public DataMap add(SysApi api) {
        try {
            api.setId(null);
            SysApi savedApi = sysApiRepository.save(api);
            Long rowNum = sysApiRepository.findRowNumById(savedApi.getId());
            cacheApis();
            DataMap result = new DataMap();
            result.put("api", savedApi);
            result.put("rowNum", rowNum);
            return result;
        } catch (DataIntegrityViolationException e) {
            throw new SysException("接口已存在");
        }
    }

    @Override
    @SimpleServiceLog("删除接口")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delete(Long apiId) {
        sysApiRepository.deleteById(apiId);
        cacheApis();
    }

    @Override
    @SimpleServiceLog("修改接口")
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public DataMap edit(SysApi api) {
        Optional<SysApi> oldApi = sysApiRepository.findById(api.getId());
        if (oldApi.isPresent()) {
            SysApi newApi = oldApi.get();
            if (api.getCallPath() != null) newApi.setCallPath(api.getCallPath());
            if (api.getDescription() != null) newApi.setDescription(api.getDescription());
            if (api.getRequestMethod() != null) newApi.setRequestMethod(api.getRequestMethod());
            if (api.getUrl() != null) newApi.setUrl(api.getUrl());
            try {
                newApi = sysApiRepository.save(newApi);
            } catch (DataIntegrityViolationException e) {
                log.info(e.getMessage());
                throw new SysException("接口调用路径已存在");
            }
            cacheApis();
            return new DataMap("api", newApi);
        } else {
            throw new SysException("要修改的接口不存在");
        }
    }

    @Override
    @ServiceLog("条件查询接口分页")
    public Page<SysApi> getApisByPageConditionally(int pageNum, int pageSize, SysApiCondition condition) {
        String callPath = (condition == null || condition.getCallPath() == null) ? null : condition.getCallPath();
        String description = (condition == null || condition.getDescription() == null) ? null : condition.getDescription();
        RequestMethod requestMethod = (condition == null || condition.getRequestMethod() == null) ? null : condition.getRequestMethod();
        String url = (condition == null || condition.getUrl() == null) ? null : condition.getUrl();

        Sort sort = Sort.by(Sort.Order.asc("callPath"));
        if (pageNum >= 0 && pageSize > 0) {
            log.info("分页查询");
            Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
            return sysApiRepository.findConditionally(callPath, description, requestMethod, url, pageable);
        } else {
            log.info("全表查询");
            return sysApiRepository.findConditionally(callPath, description, requestMethod, url, Pageable.unpaged(sort));
        }
    }
}
