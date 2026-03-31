package fun.xianlai.system.service.core.controller;

import fun.xianlai.system.service.core.service.ConfigService;
import fun.xianlai.system.service.core.service.TenantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/core/init")
public class InitController {
    @Autowired
    private TenantService tenantService;
    @Autowired
    private ConfigService configService;
//    @Autowired
//    private PathService pathService;
//    @Autowired
//    private MenuService menuService;
//    @Autowired
//    private ApiService apiService;
//    @Autowired
//    private RouteService routeService;
//
//    @ApiLog("获取初始化数据")
//    @GetMapping("/getInitData")
//    public RetResult<?> getInitData(@RequestParam String domain) {
//        log.info("INPUT: domain={}", domain);
//        XLTenant tenant = tenantService.getTenantByDomain(domain);
//        return new RetResult().success()
//                .addData("tenant", tenant)
//                .addData("configs", configService.getFrontLoadConfigsOfTenant(tenant.getId()));
//////                .addData("paths", pathService.getPathsFromCache())
//                .addData("routes", routeService.getRoutesFromCache())
//                .addData("menus", menuService.getActiveMenusFromCache())
//                .addData("apis", apiService.getApisFromCache());
//    }
}
