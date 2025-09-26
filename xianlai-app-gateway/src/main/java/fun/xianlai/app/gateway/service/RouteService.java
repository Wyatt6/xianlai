package fun.xianlai.app.gateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class RouteService implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher publisher;
    @Autowired
    private RouteDefinitionWriter writer;
    @Autowired
    private RouteDefinitionLocator locator;

    /**
     * 添加单个路由
     *
     * @param definition 路由
     */
    public void add(RouteDefinition definition) {
        log.info("添加路由 {}", definition);
        writer.save(Mono.just(definition)).subscribe();
        publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * 清空路由表
     */
    public void clear() {
        log.info("清空路由表...");
        List<RouteDefinition> routes = locator.getRouteDefinitions().buffer().blockFirst();
        if (routes != null && !routes.isEmpty()) {
            routes.forEach(definition -> {
                log.info("删除路由 {}", definition);
                writer.delete(Mono.just(definition.getId())).subscribe();
            });
        }
        log.info("路由表已清空");
        publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        publisher = applicationEventPublisher;
    }
}
