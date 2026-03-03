package fun.xianlai.app.gateway.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class DynamicRouteService {
    @Value("${nacos.group}")
    private String GROUP;
    @Value("${nacos.data-id}")
    private String DATA_ID;
    @Value("${nacos.timeout:1000}") // 当不存在该属性时默认是1000ms
    private Long TIMEOUT;

    @Autowired
    private NacosConfigService nacosConfigService;
    @Autowired
    private RouteService routeService;

    @PostConstruct
    public void initRoutes() {
        ConfigService configService = nacosConfigService.initConfigService();
        if (configService == null) {
            log.error("ConfigService初始化结果为null");
            return;
        }
        if (GROUP == null || GROUP.isBlank()) {
            log.error("找不到nacos.config.group参数");
            return;
        }
        if (DATA_ID == null || DATA_ID.isBlank()) {
            log.error("找不到nacos.config.data-id参数");
            return;
        }

        try {
            log.info("注册动态路由监听器...");
            String routeConfigs = configService.getConfigAndSignListener(DATA_ID, GROUP, TIMEOUT,
                    new Listener() {
                        @Override
                        public Executor getExecutor() {
                            return null;
                        }

                        /**
                         * 当接收到配置变化时的处理
                         *
                         * @param configInfo 路由配置信息，同routeConfig
                         */
                        @Override
                        public void receiveConfigInfo(String configInfo) {
                            log.info("监听到路由配置变化，更新路由表...");
                            if (configInfo != null && !configInfo.isBlank()) {
                                log.info("路由配置: {}", configInfo);
                                List<RouteDefinition> definitions = JSON.parseArray(configInfo, RouteDefinition.class);
                                routeService.clear();
                                for (RouteDefinition definition : definitions) {
                                    routeService.add(definition);
                                }
                                log.info("路由表已更新");
                            } else {
                                log.warn("路由配置为空");
                            }
                        }
                    });
            log.info("动态路由监听器已注册");

            log.info("初始化路由表...");
            if (routeConfigs != null && !routeConfigs.isBlank()) {
                log.info("路由配置: {}", routeConfigs);
                List<RouteDefinition> definitions = JSON.parseArray(routeConfigs, RouteDefinition.class);
                routeService.clear();
                for (RouteDefinition definition : definitions) {
                    routeService.add(definition);
                }
                log.info("路由表已初始化");
            } else {
                log.warn("路由配置为空");
            }
        } catch (NacosException e) {
            log.error("动态路由监听器注册或路由表初始化异常");
            log.error("{}", e.getMessage());
        }
    }
}
