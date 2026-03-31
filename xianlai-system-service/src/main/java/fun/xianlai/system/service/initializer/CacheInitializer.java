package fun.xianlai.system.service.initializer;

import fun.xianlai.system.service.core.service.ConfigService;
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
    private ConfigService configService;
//    @Autowired
//    private OptionService optionService;
//    @Autowired
//    private PathService pathService;
//    @Autowired
//    private RouteService routeService;
//    @Autowired
//    private MenuService menuService;
//    @Autowired
//    private ApiService apiService;
//
//    @Autowired
//    private PermissionService permissionService;
//    @Autowired
//    private RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化缓存......");
//        optionService.updateFrontLoadSystemOptionsCache();
//        optionService.updateBackLoadSystemOptionsCache();
//        pathService.updatePathsCache();
//        routeService.updateRoutesCache();
//        menuService.updateActiveMenusCache();
//        apiService.updateApisCache();
//        permissionService.setPermissionDbRefreshTime(DateUtils.now());
//        roleService.setRoleDbRefreshTime(DateUtils.now());
        log.info("......缓存初始化完成");
    }
}
