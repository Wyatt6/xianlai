package fun.xianlai.system.core.service.impl;

import fun.xianlai.common.constant.PathConst;
import fun.xianlai.system.core.entity.XLPath;
import fun.xianlai.system.core.repository.XLPathRepository;
import fun.xianlai.system.core.service.PathService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class PathServiceImpl implements PathService {
    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private XLPathRepository pathRepository;

    @Override
    public void cachePaths() {
        List<XLPath> paths = pathRepository.findAll();
        redis.opsForValue().set(PathConst.PATH_CACHE_KEY, paths, Duration.ofHours(PathConst.DEFAULT_CACHE_HOURS));
        log.info("路径缓存完成");
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<XLPath> getPathsFromCache() {
        if (!redis.hasKey(PathConst.PATH_CACHE_KEY)) {
            this.cachePaths();
        }
        return (List<XLPath>) redis.opsForValue().get(PathConst.PATH_CACHE_KEY);
    }
}
