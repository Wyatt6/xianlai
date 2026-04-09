package fun.xianlai.system.core.service;

import fun.xianlai.system.core.entity.XLRoute;

import java.util.List;

/**
 * @author WyattLau
 */
public interface RouteService {
    /**
     * 缓存路由
     */
    void cacheRoutes();

    /**
     * 获取路由森林
     */
    List<XLRoute> getForest();
}
