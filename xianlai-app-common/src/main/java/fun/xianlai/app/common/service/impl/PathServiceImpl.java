package fun.xianlai.app.common.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.app.common.model.entity.SysPath;
import fun.xianlai.app.common.repository.SysPathRepository;
import fun.xianlai.app.common.service.PathService;
import fun.xianlai.core.annotation.ServiceLog;
import fun.xianlai.core.annotation.SimpleServiceLog;
import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.response.DataMap;
import fun.xianlai.core.utils.bean.BeanUtils;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

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
    @SimpleServiceLog("缓存路径")
    @Transactional
    public void cachePaths() {
        List<SysPath> paths = sysPathRepository.findAll();
        redis.opsForValue().set("pathsChecksum", ChecksumUtil.sha256Checksum(JSONObject.toJSONString(paths)), Duration.ofHours(CACHE_HOURS));
        redis.opsForValue().set("paths", paths, Duration.ofHours(CACHE_HOURS));
    }

    @Override
    @SimpleServiceLog("从缓存获取路径")
    public List<SysPath> getPathsFromCache() {
        List<SysPath> paths = (List<SysPath>) redis.opsForValue().get("paths");
        if (paths == null) {
            self.cachePaths();
            paths = (List<SysPath>) redis.opsForValue().get("paths");
        }
        return paths;
    }

    @Override
    @ServiceLog("新增路径")
    @Transactional
    public DataMap add(SysPath path) {
        try {
            path.setId(null);
            SysPath savedPath = sysPathRepository.save(path);
            Long rowNum = sysPathRepository.findRowNumById(savedPath.getId());
            self.cachePaths();
            DataMap result = new DataMap();
            result.put("path", savedPath);
            result.put("rowNum", rowNum);
            return result;
        } catch (DataIntegrityViolationException e) {
            throw new SysException("路径名称或路径URL已存在");
        }
    }
}
