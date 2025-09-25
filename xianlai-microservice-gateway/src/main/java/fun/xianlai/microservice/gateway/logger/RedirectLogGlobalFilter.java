package fun.xianlai.microservice.gateway.logger;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

/**
 * 在每个请求到来的时候先生成traceId，打印前置日志，再重定向到目标服务
 *
 * @author WyattLau
 */
@Slf4j
@Component
public class RedirectLogGlobalFilter implements GlobalFilter, Ordered {
    private static final String BEGIN_TIME = "beginTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 转发的报文头添加traceId用于跟踪请求
        // WebFlux框架不能像Web框架那样继承OnePerRequestFilter重写doFilterInternal即可
        // 因为Filter和业务逻辑不在同一个线程，无法实现基于线程的MDC存储traceId
        // 需要用这种放入当前上下文的方式，类似于ThreadLocal
        String traceId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
        ServerHttpRequest request = exchange.getRequest().mutate().header("traceId", traceId).build();
        exchange.mutate().request(request).build();
        // traceId保存在日志框架的MDC上下文中，方便gateway的其他地方获取使用
        MDC.put("traceId", traceId);
        log.info("traceId: {}", exchange.getRequest().getHeaders().get("traceId"));
        // 记录开始时间
        exchange.getAttributes().put(BEGIN_TIME, new Date());

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            log.info("转发到: {} {}",
                    exchange.getAttribute("org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayPredicateMatchedPathRouteIdAttr").toString(),
                    exchange.getAttribute("org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayRequestUrl").toString()
            );
            Date beginTime = exchange.getAttribute(BEGIN_TIME);
            if (beginTime != null) {
                log.info("请求处理结束，耗时: {}", (new Date().compareTo(beginTime)) + "ms");
            }
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
