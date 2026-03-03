package fun.xianlai.system.initializer;

import fun.xianlai.core.utils.time.DateUtils;
import fun.xianlai.system.service.ApiService;
import fun.xianlai.system.service.MenuService;
import fun.xianlai.system.service.OptionService;
import fun.xianlai.system.service.PathService;
import fun.xianlai.system.service.RouteService;
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

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化Common模块缓存");
        optionService.cacheFrontLoadOptions();
        optionService.cacheBackLoadOptions();
        pathService.cachePaths();
        routeService.cacheRoutes();
        menuService.cacheActiveMenus();
        apiService.cacheApis();
        log.info("已完成Common模块缓存初始化");

        log.info("开始初始化IAM模块缓存");
        permissionService.setPermissionDbRefreshTime(DateUtils.now());
        roleService.setRoleDbRefreshTime(DateUtils.now());
        log.info("已完成IAM模块缓存初始化");
    }
}
