package fun.xianlai.app.common.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import fun.xianlai.app.common.model.entity.SysRoute;
import fun.xianlai.app.common.service.RouteService;
import fun.xianlai.core.annotation.ApiLog;
import fun.xianlai.core.response.RetResult;
import fun.xianlai.core.utils.EntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @ApiLog("新增路由")
    @SaCheckLogin
    @SaCheckPermission("route:add")
    @PostMapping("/add")
    public RetResult add(@RequestBody SysRoute form) {
        log.info("请求参数: {}", form);
        EntityUtil.trimString(form);
        return new RetResult().success().setData(routeService.add(form));
    }

    @ApiLog("删除路由")
    @SaCheckLogin
    @SaCheckPermission("route:delete")
    @GetMapping("/delete")
    public RetResult delete(@RequestParam Long routeId) {
        log.info("请求参数: routeId=[{}]", routeId);
        routeService.delete(routeId);
        return new RetResult().success();
    }

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
