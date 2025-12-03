package fun.xianlai.app.common.service;

import fun.xianlai.app.common.model.entity.SysMenu;
import fun.xianlai.core.response.DataMap;

import java.util.List;
import java.util.Map;

/**
 * @author WyattLau
 */
public interface MenuService {
    /**
     * 新增菜单
     */
    DataMap add(SysMenu menu);
}
