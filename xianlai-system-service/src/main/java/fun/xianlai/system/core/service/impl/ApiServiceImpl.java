package fun.xianlai.system.core.service.impl;

import fun.xianlai.common.constant.ApiConst;
import fun.xianlai.system.core.entity.XLApi;
import fun.xianlai.system.core.repository.XLApiRepository;
import fun.xianlai.system.core.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
    public void cacheApis() {
        List<XLApi> apis = apiRepository.findAll();
        redis.opsForValue().set(ApiConst.API_CACHE_KEY, apis, Duration.ofHours(ApiConst.DEFAULT_CACHE_HOURS));
        log.info("接口数据缓存完成");
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<XLApi> getApisFromCache() {
        if (!redis.hasKey(ApiConst.API_CACHE_KEY)) {
            this.cacheApis();
        }
        return (List<XLApi>) redis.opsForValue().get(ApiConst.API_CACHE_KEY);
    }
}
