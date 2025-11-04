package fun.xianlai.app.common.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.app.common.model.entity.SysRoute;
import fun.xianlai.app.common.repository.SysRouteRepository;
import fun.xianlai.app.common.service.RouteService;
import fun.xianlai.core.annotation.ServiceLog;
import fun.xianlai.core.annotation.SimpleServiceLog;
import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.response.DataMap;
import fun.xianlai.core.utils.ChecksumUtil;
import fun.xianlai.core.utils.EntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class RouteServiceImpl implements RouteService {
    private static final long CACHE_HOURS = 720L;   // 30天

    @Autowired
    private RedisTemplate<String, Object> redis;
    @Lazy
    @Autowired
    private RouteService self;
    @Autowired
    private SysRouteRepository sysRouteRepository;

    @Override
    @SimpleServiceLog("缓存路由")
    @Transactional
    public void cacheRoutes() {
        List<SysRoute> routes = self.getRouteForest();
        redis.opsForValue().set("routesChecksum", ChecksumUtil.sha256Checksum(JSONObject.toJSONString(routes)), Duration.ofHours(CACHE_HOURS));
        redis.opsForValue().set("routes", routes, Duration.ofHours(CACHE_HOURS));
    }

    @Override
    @SimpleServiceLog("从缓存获取路由")
    public List<Map<String, Object>> getRoutesFromCache() {
        List<Map<String, Object>> routes = (List<Map<String, Object>>) redis.opsForValue().get("routes");
        if (routes == null) {
            self.cacheRoutes();
            routes = (List<Map<String, Object>>) redis.opsForValue().get("routes");
        }
        return routes;
    }

    @Override
    @ServiceLog("新增路由")
    @Transactional
    public DataMap add(SysRoute route) {
        try {
            route.setId(null);
            SysRoute savedRoute = sysRouteRepository.save(route);
            self.cacheRoutes();
            DataMap result = new DataMap();
            result.put("route", savedRoute);
            return result;
        } catch (DataIntegrityViolationException e) {
            throw new SysException("路由名称已存在");
        }
    }

    @Override
    @ServiceLog("删除路由")
    @Transactional
    public void delete(Long routeId) {
        List<SysRoute> sonRoutes = sysRouteRepository.findByParentId(routeId);
        if (sonRoutes == null || sonRoutes.isEmpty()) {
            sysRouteRepository.deleteById(routeId);
            self.cacheRoutes();
        } else {
            throw new SysException("当前路由仍然包含子路由，无法删除");
        }
    }

    @Override
    @ServiceLog("修改路由")
    @Transactional
    public DataMap edit(SysRoute route) {
        Optional<SysRoute> oldRoute = sysRouteRepository.findById(route.getId());
        if (oldRoute.isPresent()) {
            SysRoute newRoute = oldRoute.get();
            EntityUtil.convertNotNull(route, newRoute);
            if (newRoute.getParentId().equals(newRoute.getId())) {
                throw new SysException("上级路由不能设置为自己");
            }
            if (newRoute.getRedirectPathName().equals(newRoute.getPathName())) {
                throw new SysException("路由重定向不能和路由路径相同");
            }
            try {
                newRoute = sysRouteRepository.save(newRoute);
            } catch (DataIntegrityViolationException e) {
                log.info(e.getMessage());
                throw new SysException("路由名称已存在");
            }
            self.cacheRoutes();
            return new DataMap("route", newRoute);
        } else {
            throw new SysException("要修改的路由不存在");
        }
    }

    @Override
    public List<SysRoute> getRouteForest() {
        List<SysRoute> routes = sysRouteRepository.findAll(Sort.by(Sort.Order.asc("sortId")));
        List<SysRoute> forest = new ArrayList<>();
        Map<Long, SysRoute> finder = new HashMap<>();
        for (SysRoute route : routes) {
            finder.put(route.getId(), route);
        }
        for (SysRoute route : routes) {
            if (route.getParentId() == 0) {
                forest.add(finder.get(route.getId()));
            } else {
                SysRoute fatherRoute = finder.get(route.getParentId());
                fatherRoute.getChildren().add(finder.get(route.getId()));
            }
        }
        return forest;
    }
}
