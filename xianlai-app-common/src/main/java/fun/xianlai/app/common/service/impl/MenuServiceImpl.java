package fun.xianlai.app.common.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.app.common.model.entity.SysMenu;
import fun.xianlai.app.common.repository.SysMenuRepository;
import fun.xianlai.app.common.service.MenuService;
import fun.xianlai.core.annotation.SimpleServiceLog;
import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.utils.ChecksumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WyattLau
 */
@Service
public class MenuServiceImpl implements MenuService {
    private static final long CACHE_HOURS = 720L;   // 30天

    @Autowired
    private RedisTemplate<String, Object> redis;
    @Lazy
    @Autowired
    private MenuService self;
    @Autowired
    private SysMenuRepository sysMenuRepository;

    @Override
    @SimpleServiceLog("缓存生效的菜单")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void cacheActiveMenus() {
        List<SysMenu> menus = getActiveMenuForest();
        redis.opsForValue().set("menusChecksum", ChecksumUtil.sha256Checksum(JSONObject.toJSONString(menus)), Duration.ofHours(CACHE_HOURS));
        redis.opsForValue().set("menus", menus, Duration.ofHours(CACHE_HOURS));
    }

    @Override
    @SimpleServiceLog("从缓存获取生效的菜单")
    public List<Map<String, Object>> getActiveMenusFromCache() {
        List<Map<String, Object>> menus = (List<Map<String, Object>>) redis.opsForValue().get("menus");
        if (menus == null) {
            self.cacheActiveMenus();
            menus = (List<Map<String, Object>>) redis.opsForValue().get("menus");
        }
        return menus;
    }

    @Override
    @SimpleServiceLog("删除菜单")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delete(Long menuId) {
        List<SysMenu> sonMenus = sysMenuRepository.findByParentId(menuId);
        if (sonMenus == null || sonMenus.isEmpty()) {
            sysMenuRepository.deleteById(menuId);
            self.cacheActiveMenus();
        } else {
            throw new SysException("当前菜单仍然包含子菜单，无法删除");
        }
    }

    @Override
    @SimpleServiceLog("获取菜单森林")
    public List<SysMenu> getMenuForest() {
        List<SysMenu> menus = sysMenuRepository.findAll(Sort.by(Sort.Order.asc("sortId")));
        List<SysMenu> forest = new ArrayList<>();
        Map<Long, SysMenu> finder = new HashMap<>();
        for (SysMenu menu : menus) {
            finder.put(menu.getId(), menu);
        }
        for (SysMenu menu : menus) {
            if (menu.getParentId() == 0) {
                forest.add(finder.get(menu.getId()));
            } else {
                SysMenu fatherMenu = finder.get(menu.getParentId());
                fatherMenu.getChildren().add(finder.get(menu.getId()));
            }
        }
        return forest;
    }

    @Override
    @SimpleServiceLog("获取生效中的菜单森林")
    public List<SysMenu> getActiveMenuForest() {
        List<SysMenu> menus = sysMenuRepository.findByActive(true, Sort.by(Sort.Order.asc("sortId")));
        List<SysMenu> forest = new ArrayList<>();
        Map<Long, SysMenu> finder = new HashMap<>();
        for (SysMenu menu : menus) {
            finder.put(menu.getId(), menu);
        }
        for (SysMenu menu : menus) {
            if (menu.getParentId() == 0) {
                forest.add(finder.get(menu.getId()));
            } else {
                SysMenu fatherMenu = finder.get(menu.getParentId());
                fatherMenu.getChildren().add(finder.get(menu.getId()));
            }
        }
        return forest;
    }
}
