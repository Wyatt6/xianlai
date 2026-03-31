package fun.xianlai.system.service.core.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/menu")
public class MenuController {
//    @Autowired
//    private MenuService menuService;
//
//    @ApiLog("新增菜单")
//    @SaCheckLogin
//    @SaCheckPermission("menu:add")
//    @PostMapping("/add")
//    public RetResult add(@RequestBody XLMenu form) {
//        log.info("请求参数: {}", form);
//        BeanUtils.trimString(form);
//        return new RetResult().success().setData(menuService.add(form));
//    }
//
//    @ApiLog("删除菜单")
//    @SaCheckLogin
//    @SaCheckPermission("menu:delete")
//    @GetMapping("/delete")
//    public RetResult delete(@RequestParam Long menuId) {
//        log.info("请求参数: menuId=[{}]", menuId);
//        menuService.delete(menuId);
//        return new RetResult().success();
//    }
//
//    @ApiLog("修改菜单")
//    @SaCheckLogin
//    @SaCheckPermission("menu:edit")
//    @PostMapping("/edit")
//    public RetResult edit(@RequestBody XLMenu form) {
//        log.info("请求参数: {}", form);
//        BeanUtils.trimString(form);
//        return new RetResult().success().setData(menuService.edit(form));
//    }
//
//    @ApiLog("重载菜单缓存")
//    @SaCheckLogin
//    @SaCheckPermission("menu:edit")
//    @GetMapping("/reloadCache")
//    public RetResult reloadCache() {
//        menuService.cacheActiveMenus();
//        return new RetResult().success();
//    }
//
//    @ApiLog("查询菜单森林")
//    @SaCheckLogin
//    @SaCheckPermission("menu:query")
//    @GetMapping("/getForest")
//    public RetResult getForest() {
//        return new RetResult().success().addData("menus", menuService.getForest());
//    }
}
