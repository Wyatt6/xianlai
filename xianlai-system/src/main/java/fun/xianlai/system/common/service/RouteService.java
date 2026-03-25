package fun.xianlai.system.common.service;

import fun.xianlai.system.common.model.entity.Route;
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
    DataMap add(Route route);

    /**
     * 删除路由
     */
    void delete(Long routeId);

    /**
     * 修改路由
     */
    DataMap edit(Route route);

    /**
     * 获取路由森林
     */
    List<Route> getForest();
}
