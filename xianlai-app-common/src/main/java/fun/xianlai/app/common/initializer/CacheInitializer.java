package fun.xianlai.app.common.initializer;

import fun.xianlai.app.common.service.ApiService;
import fun.xianlai.app.common.service.MenuService;
import fun.xianlai.app.common.service.OptionService;
import fun.xianlai.app.common.service.PathService;
import fun.xianlai.app.common.service.RouteService;
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
    }
}
