package fun.xianlai.app.common.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import fun.xianlai.app.common.service.RouteService;
import fun.xianlai.core.annotation.ApiLog;
import fun.xianlai.core.response.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/route")
public class RouteController {
    @Autowired
    private RouteService routeService;

    @ApiLog("重载路由缓存")
    @SaCheckLogin
    @SaCheckPermission("route:edit")
    @GetMapping("/reloadCache")
    public RetResult reloadCache() {
        routeService.cacheRoutes();
        return new RetResult().success();
    }

    @ApiLog("查询路由森林")
    @SaCheckLogin
    @SaCheckPermission("route:query")
    @GetMapping("/getForest")
    public RetResult getForest() {
        return new RetResult().success().addData("routes", routeService.getRouteForest());
    }
}
