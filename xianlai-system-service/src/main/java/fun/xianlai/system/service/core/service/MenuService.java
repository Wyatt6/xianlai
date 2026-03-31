package fun.xianlai.system.service.core.service;

import fun.xianlai.system.service.core.model.entity.XLMenu;

import java.util.List;
import java.util.Map;

/**
 * @author WyattLau
 */
public interface MenuService {
    /**
     * 更新生效菜单的缓存
     */
    void updateActiveMenusCache();

    /**
     * 从缓存获取生效的菜单
     */
    List<Map<String, Object>> getActiveMenusFromCache();
//
//    /**
//     * 新增菜单
//     */
//    DataMap add(XLMenu menu);
//
//    /**
//     * 删除菜单
//     */
//    void delete(Long menuId);
//
//    /**
//     * 修改菜单
//     */
//    DataMap edit(XLMenu menu);
//
//    /**
//     * 获取菜单森林
//     */
//    List<XLMenu> getForest();

    /**
     * 获取生效中的菜单森林
     */
    List<XLMenu> getActiveForest();
}
