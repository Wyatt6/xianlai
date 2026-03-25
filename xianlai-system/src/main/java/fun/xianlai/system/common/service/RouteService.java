package fun.xianlai.system.common.service;

import fun.xianlai.system.common.model.entity.XLRoute;

import java.util.List;
import java.util.Map;

/**
 * @author WyattLau
 */
public interface RouteService {
    /**
     * 更新路由缓存
     */
    void updateRoutesCache();

    /**
     * 从缓存获取路由
     */
    List<Map<String, Object>> getRoutesFromCache();
//
//    /**
//     * 新增路由
//     */
//    DataMap add(XLRoute route);
//
//    /**
//     * 删除路由
//     */
//    void delete(Long routeId);
//
//    /**
//     * 修改路由
//     */
//    DataMap edit(XLRoute route);

    /**
     * 获取路由森林
     */
    List<XLRoute> getForest();
}
