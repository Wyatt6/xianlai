package fun.xianlai.app.common.service;

import java.util.List;
import java.util.Map;

/**
 * @author WyattLau
 */
public interface MenuService {
    /**
     * 缓存生效的菜单
     */
    void cacheActiveMenus();

    /**
     * 从缓存获取生效的菜单
     */
    List<Map<String, Object>> getActiveMenusFromCache();
}
