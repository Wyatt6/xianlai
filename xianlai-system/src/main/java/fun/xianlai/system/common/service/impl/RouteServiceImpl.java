package fun.xianlai.system.common.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.system.common.model.entity.Route;
import fun.xianlai.system.common.repository.RouteRepository;
import fun.xianlai.system.common.service.RouteService;
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
    private RouteRepository routeRepository;

    @Override
    @SimpleServiceLog("缓存路由")
    @Transactional
    public void cacheRoutes() {
        List<Route> routes = self.getForest();
        redis.opsForValue().set("routesChecksum", ChecksumUtils.sha256Checksum(JSONObject.toJSONString(routes)), Duration.ofHours(CACHE_HOURS));
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
    public DataMap add(Route route) {
        try {
            route.setId(null);
            Route savedRoute = routeRepository.save(route);
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
        List<Route> sonRoutes = routeRepository.findByParentId(routeId);
        if (sonRoutes == null || sonRoutes.isEmpty()) {
            routeRepository.deleteById(routeId);
            self.cacheRoutes();
        } else {
            throw new SysException("当前路由仍然包含子路由，无法删除");
        }
    }

    @Override
    @ServiceLog("修改路由")
    @Transactional
    public DataMap edit(Route route) {
        Optional<Route> oldRoute = routeRepository.findById(route.getId());
        if (oldRoute.isPresent()) {
            Route newRoute = oldRoute.get();
            BeanUtils.copyPropertiesNotNull(route, newRoute);
            if (newRoute.getParentId().equals(newRoute.getId())) {
                throw new SysException("上级路由不能设置为自己");
            }
            if (newRoute.getRedirectPathName().equals(newRoute.getPathName())) {
                throw new SysException("路由重定向不能和路由路径相同");
            }
            try {
                newRoute = routeRepository.save(newRoute);
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
    public List<Route> getForest() {
        List<Route> routes = routeRepository.findAll(Sort.by(Sort.Order.asc("sortId")));
        List<Route> forest = new ArrayList<>();
        Map<Long, Route> finder = new HashMap<>();
        for (Route route : routes) {
            finder.put(route.getId(), route);
        }
        for (Route route : routes) {
            if (route.getParentId() == 0) {
                forest.add(finder.get(route.getId()));
            } else {
                Route fatherRoute = finder.get(route.getParentId());
                fatherRoute.getChildren().add(finder.get(route.getId()));
            }
        }
        return forest;
    }
}
