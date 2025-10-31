package fun.xianlai.app.common.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.app.common.model.entity.SysPath;
import fun.xianlai.app.common.model.form.SysPathCondition;
import fun.xianlai.app.common.repository.SysPathRepository;
import fun.xianlai.app.common.service.PathService;
import fun.xianlai.core.annotation.ServiceLog;
import fun.xianlai.core.annotation.SimpleServiceLog;
import fun.xianlai.core.utils.ChecksumUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

/**
 * @author WyattLau
 */
@Slf4j
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
    public List<SysPath> getSysPathsFromCache() {
        List<SysPath> paths = (List<SysPath>) redis.opsForValue().get("sysPaths");
        if (paths == null) {
            self.cacheSysPaths();
            paths = (List<SysPath>) redis.opsForValue().get("sysPaths");
        }
        return paths;
    }

    @Override
    @ServiceLog("条件路径接口分页")
    public Page<SysPath> getSysPathsByPageConditionally(int pageNum, int pageSize, SysPathCondition condition) {
        String name = (condition == null || condition.getName() == null) ? null : condition.getName();
        String path = (condition == null || condition.getPath() == null) ? null : condition.getPath();

        Sort sort = Sort.by(Sort.Order.asc("sortId"));
        if (pageNum >= 0 && pageSize > 0) {
            log.info("分页查询");
            Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
            return sysPathRepository.findConditionally(name, path, pageable);
        } else {
            log.info("全表查询");
            return sysPathRepository.findConditionally(name, path, Pageable.unpaged(sort));
        }
    }
}
