package fun.xianlai.app.common.service;

import java.util.List;
import java.util.Map;

/**
 * @author WyattLau
 */
public interface RouteService {
    /**
     * 缓存系统路由
     */
    void cacheSysRoutes();

    /**
     * 从缓存获取系统路由
     */
    List<Map<String, Object>> getSysRoutesFromCache();
}
