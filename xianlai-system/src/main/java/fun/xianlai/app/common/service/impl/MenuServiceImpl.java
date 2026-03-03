package fun.xianlai.app.common.service.impl;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.app.common.model.entity.SysMenu;
import fun.xianlai.app.common.repository.SysMenuRepository;
import fun.xianlai.app.common.service.MenuService;
import fun.xianlai.core.annotation.ServiceLog;
import fun.xianlai.core.annotation.SimpleServiceLog;
import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.response.DataMap;
import fun.xianlai.core.utils.ChecksumUtils;
import fun.xianlai.core.utils.bean.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static cn.dev33.satoken.SaManager.log;

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
    @Transactional
    public void cacheActiveMenus() {
        List<SysMenu> menus = self.getActiveForest();
        redis.opsForValue().set("menusChecksum", ChecksumUtils.sha256Checksum(JSONObject.toJSONString(menus)), Duration.ofHours(CACHE_HOURS));
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
    @ServiceLog("新增菜单")
    @Transactional
    public DataMap add(SysMenu menu) {
        try {
            menu.setId(null);
            SysMenu savedMenu = sysMenuRepository.save(menu);
            self.cacheActiveMenus();
            DataMap result = new DataMap();
            result.put("menu", savedMenu);
            return result;
        } catch (DataIntegrityViolationException e) {
            throw new SysException("菜单已存在");
        }
    }

    @Override
    @ServiceLog("删除菜单")
    @Transactional
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
    @ServiceLog("修改菜单")
    @Transactional
    public DataMap edit(SysMenu menu) {
        Optional<SysMenu> oldMenu = sysMenuRepository.findById(menu.getId());
        if (oldMenu.isPresent()) {
            SysMenu newMenu = oldMenu.get();
            BeanUtils.copyPropertiesNotNull(menu, newMenu);
            if (newMenu.getParentId().equals(newMenu.getId())) {
                throw new SysException("上级菜单不能设置为自己");
            }
            try {
                newMenu = sysMenuRepository.save(newMenu);
            } catch (DataIntegrityViolationException e) {
                log.info(e.getMessage());
                throw new SysException("菜单已存在");
            }
            self.cacheActiveMenus();
            return new DataMap("menu", newMenu);
        } else {
            throw new SysException("要修改的菜单不存在");
        }
    }

    @Override
    public List<SysMenu> getForest() {
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
    public List<SysMenu> getActiveForest() {
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
