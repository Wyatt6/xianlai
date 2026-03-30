package fun.xianlai.system.core.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.common.utils.ChecksumUtils;
import fun.xianlai.system.core.model.consts.ConstRouteCache;
import fun.xianlai.system.core.model.entity.XLRoute;
import fun.xianlai.system.core.repository.XLRouteRepository;
import fun.xianlai.system.core.service.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class RouteServiceImpl implements RouteService {
    @Autowired
    private RedisTemplate<String, Object> redis;
    @Lazy
    @Autowired
    private RouteService self;
    @Autowired
    private XLRouteRepository routeRepository;

    @Override
    @Transactional
    public void updateRoutesCache() {
        List<XLRoute> routes = self.getForest();
        redis.opsForValue().set(ConstRouteCache.ROUTE_CHECKSUM_CACHE_KEY, ChecksumUtils.sha256Checksum(JSONObject.toJSONString(routes)), Duration.ofHours(ConstRouteCache.ROUTE_CACHE_HOURS));
        redis.opsForValue().set(ConstRouteCache.ROUTE_CACHE_KEY, routes, Duration.ofHours(ConstRouteCache.ROUTE_CACHE_HOURS));
    }

    @Override
    public List<Map<String, Object>> getRoutesFromCache() {
        List<Map<String, Object>> routes = (List<Map<String, Object>>) redis.opsForValue().get(ConstRouteCache.ROUTE_CACHE_KEY);
        if (routes == null) {
            self.updateRoutesCache();
            routes = (List<Map<String, Object>>) redis.opsForValue().get(ConstRouteCache.ROUTE_CACHE_KEY);
        }
        return routes;
    }
//
//    @Override
//    @ServiceLog("新增路由")
//    @Transactional
//    public DataMap add(XLRoute route) {
//        try {
//            route.setId(null);
//            XLRoute savedRoute = routeRepository.save(route);
//            self.cacheRoutes();
//            DataMap result = new DataMap();
//            result.put("route", savedRoute);
//            return result;
//        } catch (DataIntegrityViolationException e) {
//            throw new SysException("路由名称已存在");
//        }
//    }
//
//    @Override
//    @ServiceLog("删除路由")
//    @Transactional
//    public void delete(Long routeId) {
//        List<XLRoute> sonRoutes = routeRepository.findByParentId(routeId);
//        if (sonRoutes == null || sonRoutes.isEmpty()) {
//            routeRepository.deleteById(routeId);
//            self.cacheRoutes();
//        } else {
//            throw new SysException("当前路由仍然包含子路由，无法删除");
//        }
//    }
//
//    @Override
//    @ServiceLog("修改路由")
//    @Transactional
//    public DataMap edit(XLRoute route) {
//        Optional<XLRoute> oldRoute = routeRepository.findById(route.getId());
//        if (oldRoute.isPresent()) {
//            XLRoute newRoute = oldRoute.get();
//            BeanUtils.copyPropertiesNotNull(route, newRoute);
//            if (newRoute.getParentId().equals(newRoute.getId())) {
//                throw new SysException("上级路由不能设置为自己");
//            }
//            if (newRoute.getRedirectPathName().equals(newRoute.getPathName())) {
//                throw new SysException("路由重定向不能和路由路径相同");
//            }
//            try {
//                newRoute = routeRepository.save(newRoute);
//            } catch (DataIntegrityViolationException e) {
//                log.info(e.getMessage());
//                throw new SysException("路由名称已存在");
//            }
//            self.cacheRoutes();
//            return new DataMap("route", newRoute);
//        } else {
//            throw new SysException("要修改的路由不存在");
//        }
//    }
//
    @Override
    public List<XLRoute> getForest() {
        List<XLRoute> routes = routeRepository.findAll(Sort.by(Sort.Order.asc("sortId")));
        List<XLRoute> forest = new ArrayList<>();
        Map<Long, XLRoute> finder = new HashMap<>();
        for (XLRoute route : routes) {
            finder.put(route.getId(), route);
        }
        for (XLRoute route : routes) {
            if (route.getParentId() == 0) {
                forest.add(finder.get(route.getId()));
            } else {
                XLRoute fatherRoute = finder.get(route.getParentId());
                fatherRoute.getChildren().add(finder.get(route.getId()));
            }
        }
        return forest;
    }
}
