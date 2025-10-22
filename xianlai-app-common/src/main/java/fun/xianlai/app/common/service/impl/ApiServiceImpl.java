package fun.xianlai.app.common.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.app.common.model.entity.SysApi;
import fun.xianlai.app.common.repository.SysApiRepository;
import fun.xianlai.app.common.service.ApiService;
import fun.xianlai.core.annotation.SimpleServiceLog;
import fun.xianlai.core.utils.ChecksumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

/**
 * @author WyattLau
 */
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
    @SimpleServiceLog("缓存系统接口")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void cacheSysApis() {
        List<SysApi> apis = sysApiRepository.findAll();
        redis.opsForValue().set("sysApisChecksum", ChecksumUtil.sha256Checksum(JSONObject.toJSONString(apis)), Duration.ofHours(CACHE_HOURS));
        redis.opsForValue().set("sysApis", apis, Duration.ofHours(CACHE_HOURS));
    }

    @Override
    @SimpleServiceLog("从缓存获取系统接口")
    public List<SysApi> getSysApisFromCache() {
        List<SysApi> apis = (List<SysApi>) redis.opsForValue().get("sysApis");
        if (apis == null) {
            self.cacheSysApis();
            apis = (List<SysApi>) redis.opsForValue().get("sysApis");
        }
        return apis;
    }
}
