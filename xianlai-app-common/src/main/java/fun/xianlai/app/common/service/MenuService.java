package fun.xianlai.app.common.service;

import fun.xianlai.app.common.model.entity.SysMenu;

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

    /**
     * 获取菜单森林
     */
    List<SysMenu> getMenuForest();

    /**
     * 获取生效中的菜单森林
     */
    List<SysMenu> getActiveMenuForest();
}
