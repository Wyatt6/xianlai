package fun.xianlai.system.core.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.common.starter.utils.ChecksumUtils;
import fun.xianlai.system.core.model.consts.ConstPathCache;
import fun.xianlai.system.core.model.entity.XLPath;
import fun.xianlai.system.core.repository.XLPathRepository;
import fun.xianlai.system.core.service.PathService;
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
public class PathServiceImpl implements PathService {
    @Autowired
    private RedisTemplate<String, Object> redis;
    @Lazy
    @Autowired
    private PathService self;
    @Autowired
    private XLPathRepository pathRepository;

    @Override
    @Transactional
    public void updatePathsCache() {
        List<XLPath> paths = pathRepository.findAll();
        redis.opsForValue().set(ConstPathCache.PATH_CHECKSUM_CACHE_KEY, ChecksumUtils.sha256Checksum(JSONObject.toJSONString(paths)), Duration.ofHours(ConstPathCache.PATH_CACHE_HOURS));
        redis.opsForValue().set(ConstPathCache.PATH_CACHE_KEY, paths, Duration.ofHours(ConstPathCache.PATH_CACHE_HOURS));
    }

    @Override
    public List<XLPath> getPathsFromCache() {
        List<XLPath> paths = (List<XLPath>) redis.opsForValue().get(ConstPathCache.PATH_CACHE_KEY);
        if (paths == null) {
            self.updatePathsCache();
            paths = (List<XLPath>) redis.opsForValue().get(ConstPathCache.PATH_CACHE_KEY);
        }
        return paths;
    }
//
//    @Override
//    @ServiceLog("新增路径")
//    @Transactional
//    public DataMap add(XLPath path) {
//        try {
//            path.setId(null);
//            XLPath savedPath = pathRepository.save(path);
//            Long rowNum = pathRepository.findRowNumById(savedPath.getId());
//            self.cachePaths();
//            DataMap result = new DataMap();
//            result.put("path", savedPath);
//            result.put("rowNum", rowNum);
//            return result;
//        } catch (DataIntegrityViolationException e) {
//            throw new SysException("路径名称或路径URL已存在");
//        }
//    }
//
//    @Override
//    @ServiceLog("删除路径")
//    @Transactional
//    public void delete(Long pathId) {
//        pathRepository.deleteById(pathId);
//        self.cachePaths();
//    }
//
//    @Override
//    @ServiceLog("修改路径")
//    @Transactional
//    public DataMap edit(XLPath path) {
//        Optional<XLPath> oldPath = pathRepository.findById(path.getId());
//        if (oldPath.isPresent()) {
//            XLPath newPath = oldPath.get();
//            BeanUtils.copyPropertiesNotNull(path, newPath);
//            try {
//                newPath = pathRepository.save(newPath);
//            } catch (DataIntegrityViolationException e) {
//                log.info(e.getMessage());
//                throw new SysException("路径名称或路径URL已存在");
//            }
//            self.cachePaths();
//            return new DataMap("path", newPath);
//        } else {
//            throw new SysException("要修改的路径不存在");
//        }
//    }
//
//    @Override
//    @ServiceLog("条件查询路径分页")
//    public Page<XLPath> getPageConditionally(int pageNum, int pageSize, XLPath condition) {
//        String name = BeanUtils.getFieldValue(condition, "name", String.class);
//        String path = BeanUtils.getFieldValue(condition, "path", String.class);
//
//        Sort sort = Sort.by(Sort.Order.asc("sortId"), Sort.Order.asc("name"));
//        if (pageNum >= 0 && pageSize > 0) {
//            log.info("分页查询");
//            Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
//            return pathRepository.findConditionally(name, path, pageable);
//        } else {
//            log.info("全表查询");
//            return pathRepository.findConditionally(name, path, Pageable.unpaged(sort));
//        }
//    }
}
