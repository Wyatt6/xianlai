package fun.xianlai.system.core.service.impl;

import fun.xianlai.common.constant.RouteConst;
import fun.xianlai.system.core.entity.XLRoute;
import fun.xianlai.system.core.repository.XLRouteRepository;
import fun.xianlai.system.core.service.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private XLRouteRepository routeRepository;

    @Override
    @Transactional
    public void cacheRoutes() {
        List<XLRoute> routes = this.getForest();
        redis.opsForValue().set(RouteConst.ROUTE_CACHE_KEY, routes, Duration.ofHours(RouteConst.DEFAULT_CACHE_HOURS));
        log.info("路由数据缓存完成");
    }

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
