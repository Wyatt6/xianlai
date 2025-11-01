package fun.xianlai.app.common.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import fun.xianlai.app.common.service.MenuService;
import fun.xianlai.core.annotation.ApiLog;
import fun.xianlai.core.response.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @ApiLog("删除菜单")
    @SaCheckLogin
    @SaCheckPermission("menu:delete")
    @GetMapping("/delete")
    public RetResult delete(@RequestParam Long menuId) {
        log.info("请求参数: menuId=[{}]", menuId);
        menuService.delete(menuId);
        return new RetResult().success();
    }

    @ApiLog("重载菜单缓存")
    @SaCheckLogin
    @SaCheckPermission("menu:edit")
    @GetMapping("/reloadCache")
    public RetResult reloadCache() {
        menuService.cacheActiveMenus();
        return new RetResult().success();
    }

    @ApiLog("查询菜单森林")
    @SaCheckLogin
    @SaCheckPermission("menu:query")
    @GetMapping("/getForest")
    public RetResult getForest() {
        return new RetResult().success().addData("menus", menuService.getMenuForest());
    }
}
