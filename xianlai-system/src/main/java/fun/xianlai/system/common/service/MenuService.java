package fun.xianlai.system.common.service;

import fun.xianlai.system.common.model.entity.Menu;
import fun.xianlai.core.response.DataMap;

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
     * 新增菜单
     */
    DataMap add(Menu menu);

    /**
     * 删除菜单
     */
    void delete(Long menuId);

    /**
     * 修改菜单
     */
    DataMap edit(Menu menu);

    /**
     * 获取菜单森林
     */
    List<Menu> getForest();

    /**
     * 获取生效中的菜单森林
     */
    List<Menu> getActiveForest();
}
