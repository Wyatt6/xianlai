package fun.xianlai.system.common.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.system.common.model.entity.Path;
import fun.xianlai.system.common.repository.PathRepository;
import fun.xianlai.system.common.service.PathService;
import fun.xianlai.core.annotation.ServiceLog;
import fun.xianlai.core.annotation.SimpleServiceLog;
import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.response.DataMap;
import fun.xianlai.core.utils.bean.BeanUtils;
import fun.xianlai.core.utils.ChecksumUtils;
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
    private PathRepository pathRepository;

    @Override
    @SimpleServiceLog("缓存路径")
    @Transactional
    public void cachePaths() {
        List<Path> paths = pathRepository.findAll();
        redis.opsForValue().set("pathsChecksum", ChecksumUtils.sha256Checksum(JSONObject.toJSONString(paths)), Duration.ofHours(CACHE_HOURS));
        redis.opsForValue().set("paths", paths, Duration.ofHours(CACHE_HOURS));
    }

    @Override
    @SimpleServiceLog("从缓存获取路径")
    public List<Path> getPathsFromCache() {
        List<Path> paths = (List<Path>) redis.opsForValue().get("paths");
        if (paths == null) {
            self.cachePaths();
            paths = (List<Path>) redis.opsForValue().get("paths");
        }
        return paths;
    }

    @Override
    @ServiceLog("新增路径")
    @Transactional
    public DataMap add(Path path) {
        try {
            path.setId(null);
            Path savedPath = pathRepository.save(path);
            Long rowNum = pathRepository.findRowNumById(savedPath.getId());
            self.cachePaths();
            DataMap result = new DataMap();
            result.put("path", savedPath);
            result.put("rowNum", rowNum);
            return result;
        } catch (DataIntegrityViolationException e) {
            throw new SysException("路径名称或路径URL已存在");
        }
    }

    @Override
    @ServiceLog("删除路径")
    @Transactional
    public void delete(Long pathId) {
        pathRepository.deleteById(pathId);
        self.cachePaths();
    }

    @Override
    @ServiceLog("修改路径")
    @Transactional
    public DataMap edit(Path path) {
        Optional<Path> oldPath = pathRepository.findById(path.getId());
        if (oldPath.isPresent()) {
            Path newPath = oldPath.get();
            BeanUtils.copyPropertiesNotNull(path, newPath);
            try {
                newPath = pathRepository.save(newPath);
            } catch (DataIntegrityViolationException e) {
                log.info(e.getMessage());
                throw new SysException("路径名称或路径URL已存在");
            }
            self.cachePaths();
            return new DataMap("path", newPath);
        } else {
            throw new SysException("要修改的路径不存在");
        }
    }

    @Override
    @ServiceLog("条件查询路径分页")
    public Page<Path> getPageConditionally(int pageNum, int pageSize, Path condition) {
        String name = BeanUtils.getFieldValue(condition, "name", String.class);
        String path = BeanUtils.getFieldValue(condition, "path", String.class);

        Sort sort = Sort.by(Sort.Order.asc("sortId"), Sort.Order.asc("name"));
        if (pageNum >= 0 && pageSize > 0) {
            log.info("分页查询");
            Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
            return pathRepository.findConditionally(name, path, pageable);
        } else {
            log.info("全表查询");
            return pathRepository.findConditionally(name, path, Pageable.unpaged(sort));
        }
    }
}
