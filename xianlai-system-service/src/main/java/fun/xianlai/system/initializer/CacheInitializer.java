package fun.xianlai.system.initializer;

import fun.xianlai.system.core.service.ConfigService;
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
    private ConfigService configService;
    @Autowired
    private PathService pathService;
    @Autowired
    private RouteService routeService;

    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化缓存......");
        configService.cacheSystemConfigs();
        pathService.cachePaths();
        routeService.cacheRoutes();
        log.info("......缓存初始化完成");
    }
}
