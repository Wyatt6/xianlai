package fun.xianlai.app.common.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.app.common.model.entity.SysMenu;
import fun.xianlai.app.common.model.entity.SysRoute;
import fun.xianlai.app.common.repository.SysRouteRepository;
import fun.xianlai.app.common.service.RouteService;
import fun.xianlai.basic.annotation.SimpleServiceLog;
import fun.xianlai.basic.utils.ChecksumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WyattLau
 */
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
    @SimpleServiceLog("缓存系统路由")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void cacheSysRoutes() {
        List<SysRoute> routes = sysRouteRepository.findAll(Sort.by(Sort.Order.asc("sortId")));
        List<Map<String, Object>> listRoutes = new ArrayList<>();
        if (routes != null) {
            Map<Long, Map<String, Object>> finder = new HashMap<>();
            for (SysRoute route : routes) {
                Map<String, Object> mapRoute = new HashMap<>();
                mapRoute.put("name", route.getName());
                mapRoute.put("pathName", route.getPathName());
                mapRoute.put("redirectPathName", route.getRedirectPathName());
                mapRoute.put("componentPath", route.getComponentPath());
                mapRoute.put("needLogin", route.getNeedLogin());
                mapRoute.put("needPermission", route.getNeedPermission());
                mapRoute.put("permission", route.getPermission());
                mapRoute.put("children", new ArrayList<>());
                finder.put(route.getId(), mapRoute);
            }
            for (SysRoute route : routes) {
                if (route.getParentId() == 0) {
                    listRoutes.add(finder.get(route.getId()));
                } else {
                    Map<String, Object> fatherRoute = finder.get(route.getParentId());
                    ((List) fatherRoute.get("children")).add(finder.get(route.getId()));
                }
            }
        }
        redis.opsForValue().set("sysRoutesChecksum", ChecksumUtil.sha256Checksum(JSONObject.toJSONString(listRoutes)), Duration.ofHours(CACHE_HOURS));
        redis.opsForValue().set("sysRoutes", listRoutes, Duration.ofHours(CACHE_HOURS));
    }

    @Override
    @SimpleServiceLog("从缓存获取系统路由")
    public List<Map<String, Object>> getSysRoutesFromCache() {
        List<Map<String, Object>> routes = (List<Map<String, Object>>) redis.opsForValue().get("sysRoutes");
        if (routes == null) {
            self.cacheSysRoutes();
            routes = (List<Map<String, Object>>) redis.opsForValue().get("sysRoutes");
        }
        return routes;
    }
}
