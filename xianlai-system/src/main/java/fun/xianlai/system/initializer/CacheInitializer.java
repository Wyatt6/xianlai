package fun.xianlai.system.initializer;

import fun.xianlai.system.common.service.ApiService;
import fun.xianlai.system.common.service.MenuService;
import fun.xianlai.system.common.service.OptionService;
import fun.xianlai.system.common.service.PathService;
import fun.xianlai.system.common.service.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author WyattLau
 */
@Slf4j
@Component
public class CacheInitializer implements CommandLineRunner {
    @Autowired
    private OptionService optionService;
    @Autowired
    private PathService pathService;
    @Autowired
    private RouteService routeService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private ApiService apiService;
//
//    @Autowired
//    private PermissionService permissionService;
//    @Autowired
//    private RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化缓存......");
        optionService.updateFrontLoadSystemOptionsCache();
        optionService.updateBackLoadSystemOptionsCache();
        pathService.updatePathsCache();
        routeService.updateRoutesCache();
        menuService.updateActiveMenusCache();
        apiService.updateApisCache();
//        permissionService.setPermissionDbRefreshTime(DateUtils.now());
//        roleService.setRoleDbRefreshTime(DateUtils.now());
        log.info("缓存初始化完成");
    }
}
