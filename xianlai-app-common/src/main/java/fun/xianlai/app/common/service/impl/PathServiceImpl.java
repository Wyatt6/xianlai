package fun.xianlai.app.common.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.app.common.model.entity.SysPath;
import fun.xianlai.app.common.repository.SysPathRepository;
import fun.xianlai.app.common.service.PathService;
import fun.xianlai.basic.annotation.SimpleServiceLog;
import fun.xianlai.basic.utils.ChecksumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * @author WyattLau
 */
@Service
public class PathServiceImpl implements PathService {
    private static final long CACHE_HOURS = 720L;   // 30天

    @Autowired
    private RedisTemplate<String, Object> redis;
    @Lazy
    @Autowired
    private PathService self;
    @Autowired
    private SysPathRepository sysPathRepository;

    @Override
    @SimpleServiceLog("缓存系统路径")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void cacheSysPaths() {
        List<SysPath> paths = sysPathRepository.findAll();
        redis.opsForValue().set("sysPathsChecksum", ChecksumUtil.sha256Checksum(JSONObject.toJSONString(paths)), Duration.ofHours(CACHE_HOURS));
        redis.opsForValue().set("sysPaths", paths, Duration.ofHours(CACHE_HOURS));
    }

    @Override
    @SimpleServiceLog("从缓存获取系统路径")
    public List<Map<String, String>> getSysPathsFromCache() {
        List<Map<String, String>> paths = (List<Map<String, String>>) redis.opsForValue().get("sysPaths");
        if (paths == null) {
            self.cacheSysPaths();
            paths = (List<Map<String, String>>) redis.opsForValue().get("sysPaths");
        }
        return paths;
    }
}
