package fun.xianlai.system.core.service.impl;

import fun.xianlai.common.constant.MenuConst;
import fun.xianlai.system.core.entity.XLMenu;
import fun.xianlai.system.core.repository.XLMenuRepository;
import fun.xianlai.system.core.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private XLMenuRepository menuRepository;

    @Override
    public void cacheMenus() {
        List<XLMenu> menus = this.getActiveForest();
        redis.opsForValue().set(MenuConst.MENU_CACHE_KEY, menus, Duration.ofHours(MenuConst.DEFAULT_CACHE_HOURS));
        log.info("菜单数据缓存完成");
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getMenusFromCache() {
        if (!redis.hasKey(MenuConst.MENU_CACHE_KEY)) {
            this.cacheMenus();
        }
        return (List<Map<String, Object>>) redis.opsForValue().get(MenuConst.MENU_CACHE_KEY);
    }

    @Override
    public List<XLMenu> getActiveForest() {
        List<XLMenu> menus = menuRepository.findByActive(true, Sort.by(Sort.Order.asc("sortId")));
        List<XLMenu> forest = new ArrayList<>();
        Map<Long, XLMenu> finder = new HashMap<>();
        for (XLMenu menu : menus) {
            finder.put(menu.getId(), menu);
        }
        for (XLMenu menu : menus) {
            if (menu.getParentId() == 0) {
                forest.add(finder.get(menu.getId()));
            } else {
                XLMenu fatherMenu = finder.get(menu.getParentId());
                fatherMenu.getChildren().add(finder.get(menu.getId()));
            }
        }
        return forest;
    }
}
