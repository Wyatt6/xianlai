package fun.xianlai.app.common.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.app.common.model.entity.SysPath;
import fun.xianlai.app.common.model.form.SysPathCondition;
import fun.xianlai.app.common.repository.SysPathRepository;
import fun.xianlai.app.common.service.PathService;
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
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void cacheSysPaths() {
        List<SysPath> paths = sysPathRepository.findAll();
        redis.opsForValue().set("sysPathsChecksum", ChecksumUtil.sha256Checksum(JSONObject.toJSONString(paths)), Duration.ofHours(CACHE_HOURS));
        redis.opsForValue().set("sysPaths", paths, Duration.ofHours(CACHE_HOURS));
    }

    @Override
    @SimpleServiceLog("从缓存获取路径")
    public List<SysPath> getSysPathsFromCache() {
        List<SysPath> paths = (List<SysPath>) redis.opsForValue().get("sysPaths");
        if (paths == null) {
            self.cacheSysPaths();
            paths = (List<SysPath>) redis.opsForValue().get("sysPaths");
        }
        return paths;
    }

    @Override
    @SimpleServiceLog("新增路径")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public DataMap add(SysPath path) {
        try {
            path.setId(null);
            SysPath savedPath = sysPathRepository.save(path);
            Long rowNum = sysPathRepository.findRowNumById(savedPath.getId());
            cacheSysPaths();
            DataMap result = new DataMap();
            result.put("path", savedPath);
            result.put("rowNum", rowNum);
            return result;
        } catch (DataIntegrityViolationException e) {
            throw new SysException("路径名称或路径URL已存在");
        }
    }

    @Override
    @SimpleServiceLog("删除路径")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delete(Long pathId) {
        sysPathRepository.deleteById(pathId);
        cacheSysPaths();
    }

    @Override
    @SimpleServiceLog("修改路径")
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public DataMap edit(SysPath path) {
        Optional<SysPath> oldPath = sysPathRepository.findById(path.getId());
        if (oldPath.isPresent()) {
            SysPath newPath = oldPath.get();
            if (path.getSortId() != null) newPath.setSortId(path.getSortId());
            if (path.getName() != null) newPath.setName(path.getName());
            if (path.getPath() != null) newPath.setPath(path.getPath());
            try {
                newPath = sysPathRepository.save(newPath);
            } catch (DataIntegrityViolationException e) {
                log.info(e.getMessage());
                throw new SysException("路径名称或路径URL已存在");
            }
            cacheSysPaths();
            return new DataMap("path", newPath);
        } else {
            throw new SysException("要修改的路径不存在");
        }
    }

    @Override
    @ServiceLog("条件查询路径分页")
    public Page<SysPath> getPathsByPageConditionally(int pageNum, int pageSize, SysPathCondition condition) {
        String name = (condition == null || condition.getName() == null) ? null : condition.getName();
        String path = (condition == null || condition.getPath() == null) ? null : condition.getPath();

        Sort sort = Sort.by(Sort.Order.asc("sortId"), Sort.Order.asc("name"));
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
