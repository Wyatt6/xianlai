package fun.xianlai.system.initializer;

import fun.xianlai.system.core.service.ApiService;
import fun.xianlai.system.core.service.MenuService;
import fun.xianlai.system.core.service.PathService;
import fun.xianlai.system.core.service.RouteService;
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
    private PathService pathService;
    @Autowired
    private RouteService routeService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private ApiService apiService;

    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化缓存......");
        log.info("配置数据缓存已在服务启动时完成");
        pathService.cachePaths();
        routeService.cacheRoutes();
        menuService.cacheMenus();
        apiService.cacheApis();
        log.info("......缓存初始化完成");
    }
}
