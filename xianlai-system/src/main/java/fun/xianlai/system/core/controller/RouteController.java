package fun.xianlai.system.core.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/route")
public class RouteController {
//    @Autowired
//    private RouteService routeService;
//
//    @ApiLog("新增路由")
//    @SaCheckLogin
//    @SaCheckPermission("route:add")
//    @PostMapping("/add")
//    public RetResult add(@RequestBody XLRoute form) {
//        log.info("请求参数: {}", form);
//        BeanUtils.trimString(form);
//        return new RetResult().success().setData(routeService.add(form));
//    }
//
//    @ApiLog("删除路由")
//    @SaCheckLogin
//    @SaCheckPermission("route:delete")
//    @GetMapping("/delete")
//    public RetResult delete(@RequestParam Long routeId) {
//        log.info("请求参数: routeId=[{}]", routeId);
//        routeService.delete(routeId);
//        return new RetResult().success();
//    }
//
//    @ApiLog("修改路由")
//    @SaCheckLogin
//    @SaCheckPermission("route:edit")
//    @PostMapping("/edit")
//    public RetResult edit(@RequestBody XLRoute form) {
//        log.info("请求参数: {}", form);
//        BeanUtils.trimString(form);
//        return new RetResult().success().setData(routeService.edit(form));
//    }
//
//    @ApiLog("重载路由缓存")
//    @SaCheckLogin
//    @SaCheckPermission("route:edit")
//    @GetMapping("/reloadCache")
//    public RetResult reloadCache() {
//        routeService.cacheRoutes();
//        return new RetResult().success();
//    }
//
//    @ApiLog("查询路由森林")
//    @SaCheckLogin
//    @SaCheckPermission("route:query")
//    @GetMapping("/getForest")
//    public RetResult getForest() {
//        return new RetResult().success().addData("routes", routeService.getForest());
//    }
}
