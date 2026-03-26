package fun.xianlai.system.core.controller;

import fun.xianlai.common.annotation.ApiLog;
import fun.xianlai.common.response.RetResult;
import fun.xianlai.system.core.model.entity.XLTenant;
import fun.xianlai.system.core.service.TenantService;
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
@RequestMapping("/core/init")
public class InitController {
    @Autowired
    private TenantService tenantService;
//    @Autowired
//    private OptionService optionService;
//    @Autowired
//    private PathService pathService;
//    @Autowired
//    private MenuService menuService;
//    @Autowired
//    private ApiService apiService;
//    @Autowired
//    private RouteService routeService;

    @ApiLog("获取初始化数据")
    @GetMapping("/getInitData")
    public RetResult getInitData(@RequestParam String domain) {
        XLTenant tenant = tenantService.getTenantByDomain(domain);
        return new RetResult().success()
                .addData("tenant", tenant);
//                .addData("systemOptions", optionService.getFrontLoadSystemOptionsFromCache())
//                .addData("paths", pathService.getPathsFromCache())
//                .addData("routes", routeService.getRoutesFromCache())
//                .addData("menus", menuService.getActiveMenusFromCache())
//                .addData("apis", apiService.getApisFromCache());
    }
}
