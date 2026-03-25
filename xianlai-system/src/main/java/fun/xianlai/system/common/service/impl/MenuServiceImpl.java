package fun.xianlai.system.common.service.impl;

import fun.xianlai.system.common.service.MenuService;
import org.springframework.stereotype.Service;

/**
 * @author WyattLau
 */
@Service
public class MenuServiceImpl implements MenuService {
//    @Autowired
//    private RedisTemplate<String, Object> redis;
//    @Lazy
//    @Autowired
//    private MenuService self;
//    @Autowired
//    private XLMenuRepository menuRepository;
//
//    @Override
//    @SimpleServiceLog("缓存生效的菜单")
//    @Transactional
//    public void cacheActiveMenus() {
//        List<XLMenu> menus = self.getActiveForest();
//        redis.opsForValue().set("menusChecksum", ChecksumUtils.sha256Checksum(JSONObject.toJSONString(menus)), Duration.ofHours(CACHE_HOURS));
//        redis.opsForValue().set("menus", menus, Duration.ofHours(CACHE_HOURS));
//    }
//
//    @Override
//    @SimpleServiceLog("从缓存获取生效的菜单")
//    public List<Map<String, Object>> getActiveMenusFromCache() {
//        List<Map<String, Object>> menus = (List<Map<String, Object>>) redis.opsForValue().get("menus");
//        if (menus == null) {
//            self.cacheActiveMenus();
//            menus = (List<Map<String, Object>>) redis.opsForValue().get("menus");
//        }
//        return menus;
//    }
//
//    @Override
//    @ServiceLog("新增菜单")
//    @Transactional
//    public DataMap add(XLMenu menu) {
//        try {
//            menu.setId(null);
//            XLMenu savedMenu = menuRepository.save(menu);
//            self.cacheActiveMenus();
//            DataMap result = new DataMap();
//            result.put("menu", savedMenu);
//            return result;
//        } catch (DataIntegrityViolationException e) {
//            throw new SysException("菜单已存在");
//        }
//    }
//
//    @Override
//    @ServiceLog("删除菜单")
//    @Transactional
//    public void delete(Long menuId) {
//        List<XLMenu> sonMenus = menuRepository.findByParentId(menuId);
//        if (sonMenus == null || sonMenus.isEmpty()) {
//            menuRepository.deleteById(menuId);
//            self.cacheActiveMenus();
//        } else {
//            throw new SysException("当前菜单仍然包含子菜单，无法删除");
//        }
//    }
//
//    @Override
//    @ServiceLog("修改菜单")
//    @Transactional
//    public DataMap edit(XLMenu menu) {
//        Optional<XLMenu> oldMenu = menuRepository.findById(menu.getId());
//        if (oldMenu.isPresent()) {
//            XLMenu newMenu = oldMenu.get();
//            BeanUtils.copyPropertiesNotNull(menu, newMenu);
//            if (newMenu.getParentId().equals(newMenu.getId())) {
//                throw new SysException("上级菜单不能设置为自己");
//            }
//            try {
//                newMenu = menuRepository.save(newMenu);
//            } catch (DataIntegrityViolationException e) {
//                log.info(e.getMessage());
//                throw new SysException("菜单已存在");
//            }
//            self.cacheActiveMenus();
//            return new DataMap("menu", newMenu);
//        } else {
//            throw new SysException("要修改的菜单不存在");
//        }
//    }
//
//    @Override
//    public List<XLMenu> getForest() {
//        List<XLMenu> menus = menuRepository.findAll(Sort.by(Sort.Order.asc("sortId")));
//        List<XLMenu> forest = new ArrayList<>();
//        Map<Long, XLMenu> finder = new HashMap<>();
//        for (XLMenu menu : menus) {
//            finder.put(menu.getId(), menu);
//        }
//        for (XLMenu menu : menus) {
//            if (menu.getParentId() == 0) {
//                forest.add(finder.get(menu.getId()));
//            } else {
//                XLMenu fatherMenu = finder.get(menu.getParentId());
//                fatherMenu.getChildren().add(finder.get(menu.getId()));
//            }
//        }
//        return forest;
//    }
//
//    @Override
//    public List<XLMenu> getActiveForest() {
//        List<XLMenu> menus = menuRepository.findByActive(true, Sort.by(Sort.Order.asc("sortId")));
//        List<XLMenu> forest = new ArrayList<>();
//        Map<Long, XLMenu> finder = new HashMap<>();
//        for (XLMenu menu : menus) {
//            finder.put(menu.getId(), menu);
//        }
//        for (XLMenu menu : menus) {
//            if (menu.getParentId() == 0) {
//                forest.add(finder.get(menu.getId()));
//            } else {
//                XLMenu fatherMenu = finder.get(menu.getParentId());
//                fatherMenu.getChildren().add(finder.get(menu.getId()));
//            }
//        }
//        return forest;
//    }
}
