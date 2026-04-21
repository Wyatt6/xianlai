package fun.xianlai.system.core.controller;

import fun.xianlai.common.annotation.ApiLog;
import fun.xianlai.common.response.RetResult;
import fun.xianlai.system.core.entity.XLApi;
import fun.xianlai.system.core.entity.XLPath;
import fun.xianlai.system.core.entity.XLTenant;
import fun.xianlai.system.core.service.ApiService;
import fun.xianlai.system.core.service.ConfigManageService;
import fun.xianlai.system.core.service.MenuService;
import fun.xianlai.system.core.service.PathService;
import fun.xianlai.system.core.service.RouteService;
import fun.xianlai.system.core.service.TenantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ConfigManageService configManageService;
    @Autowired
    private PathService pathService;
    @Autowired
    private RouteService routeService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private ApiService apiService;

    @ApiLog("获取初始化数据")
    @GetMapping("/getInitData")
    public RetResult<?> getInitData(@RequestParam String domain) {
        XLTenant tenant = tenantService.getTenantByDomain(domain);
        Map<String, Map<String, Object>> configs = configManageService.getTenantFrontLoadConfigs(tenant.getId());
        List<XLPath> paths = pathService.getPathsFromCache();
        List<Map<String, Object>> routes = routeService.getRoutesFromCache();
        List<Map<String, Object>> menus = menuService.getMenusFromCache();
        List<XLApi> apis = apiService.getApisFromCache();

        Map<String, Object> model = new HashMap<>();
        model.put("tenant", tenant);
        model.put("configs", configs);
        model.put("paths", paths);
        model.put("routes", routes);
        model.put("menus", menus);
        model.put("apis", apis);

        return RetResult.success(model);
    }
}
