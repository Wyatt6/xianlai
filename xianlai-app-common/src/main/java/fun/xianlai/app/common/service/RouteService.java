package fun.xianlai.app.common.service;

import fun.xianlai.app.common.model.entity.SysRoute;
import fun.xianlai.core.response.DataMap;

import java.util.List;
import java.util.Map;

/**
 * @author WyattLau
 */
public interface RouteService {
    /**
     * 缓存路由
     */
    void cacheRoutes();

    /**
     * 从缓存获取路由
     */
    List<Map<String, Object>> getRoutesFromCache();

    /**
     * 新增路由
     */
    DataMap add(SysRoute route);

    /**
     * 获取路由森林
     */
    List<SysRoute> getRouteForest();
}
