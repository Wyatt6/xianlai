package fun.xianlai.app.common.service;

import java.util.List;
import java.util.Map;

/**
 * @author WyattLau
 */
public interface MenuService {
    /**
     * 缓存生效的系统菜单
     */
    void cacheActiveSysMenus();

    /**
     * 从缓存获取生效的系统菜单
     */
    List<Map<String, Object>> getActiveSysMenusFromCache();
}
