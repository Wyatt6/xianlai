package fun.xianlai.app.common.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.app.common.model.entity.SysMenu;
import fun.xianlai.app.common.model.entity.SysOption;
import fun.xianlai.app.common.repository.SysMenuRepository;
import fun.xianlai.app.common.service.MenuService;
import fun.xianlai.basic.annotation.SimpleServiceLog;
import fun.xianlai.basic.utils.ChecksumUtil;
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
    @SimpleServiceLog("缓存生效的系统菜单")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void cacheActiveSysMenus() {
        List<SysMenu> menus = sysMenuRepository.findByActive(true, Sort.by(Sort.Order.asc("sortId")));
        List<Map<String, Object>> listMenus = new ArrayList<>();
        if (menus != null) {
            Map<Long, Map<String, Object>> finder = new HashMap<>();
            for (SysMenu menu : menus) {
                Map<String, Object> mapMenu = new HashMap<>();
                mapMenu.put("icon", menu.getIcon());
                mapMenu.put("title", menu.getTitle());
                mapMenu.put("pathName", menu.getPathName());
                mapMenu.put("permission", menu.getPermission());
                mapMenu.put("children", new ArrayList<>());
                finder.put(menu.getId(), mapMenu);
            }
            for (SysMenu menu : menus) {
                if (menu.getParentId() == 0) {
                    listMenus.add(finder.get(menu.getId()));
                } else {
                    Map<String, Object> fatherMenu = finder.get(menu.getParentId());
                    ((List) fatherMenu.get("children")).add(finder.get(menu.getId()));
                }
            }
        }
        redis.opsForValue().set("sysMenusChecksum", ChecksumUtil.sha256Checksum(JSONObject.toJSONString(listMenus)), Duration.ofHours(CACHE_HOURS));
        redis.opsForValue().set("sysMenus", listMenus, Duration.ofHours(CACHE_HOURS));
    }

    @Override
    @SimpleServiceLog("从缓存获取生效的系统菜单")
    public List<Map<String, Object>> getActiveSysMenusFromCache() {
        List<Map<String, Object>> menus = (List<Map<String, Object>>) redis.opsForValue().get("sysMenus");
        if (menus == null) {
            self.cacheActiveSysMenus();
            menus = (List<Map<String, Object>>) redis.opsForValue().get("sysMenus");
        }
        return menus;
    }
}
