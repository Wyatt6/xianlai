package fun.xianlai.system.core.service;

import fun.xianlai.system.core.entity.XLMenu;

import java.util.List;
import java.util.Map;

/**
 * @author WyattLau
 */
public interface MenuService {
    /**
     * 缓存菜单
     */
    void cacheMenus();

    /**
     * 从缓存获取菜单
     */
    List<Map<String, Object>> getMenusFromCache();

    /**
     * 获取生效中的菜单森林
     */
    List<XLMenu> getActiveForest();
}
