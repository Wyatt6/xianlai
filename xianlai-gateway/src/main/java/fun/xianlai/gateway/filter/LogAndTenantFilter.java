package fun.xianlai.gateway.filter;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * @author WyattLau
 */
@Slf4j
@Component
public class LogAndTenantFilter implements GlobalFilter, Ordered {
    private static final String[] EXCLUDE_URL_PATTERN = {
            ".*/actuator.*",
            ".*/druid.*"
    };

    private boolean shouldHandle(String url) {
        for (String regex : EXCLUDE_URL_PATTERN) {
            if (url.matches(regex)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        if (shouldHandle(path)) {
            long stTimestamp = System.currentTimeMillis();
            exchange.getAttributes().put("beginTime", stTimestamp);

            // 1. 生成traceId并打印开始日志
            String traceId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
            MDC.put("traceId", traceId);
            log.info(">>>>> 开始处理请求: {} {}", request.getMethod(), request.getURI());
            log.info("traceId: {}", traceId);

            // 2. 解析报文头token获取tenantId（尚未验签）
            String token = request.getHeaders().getFirst("token");
            String tenantId = null;
            if (token == null) {
                log.info("报文头无token");
            } else {
                try {
                    JWSObject jwsObject = JWSObject.parse(token);
                    JWTClaimsSet claims = JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());
                    tenantId = claims.getStringClaim("tenantId");
                    log.info("tenantId: {}", tenantId);
                } catch (Exception e) {
                    log.warn(e.getMessage());
                }
            }

            // 3. 组装转发请求的报文头，写入stTimestamp、traceId和tenantId
            ServerHttpRequest.Builder builder = request.mutate();
            builder.header("stTimestamp", "" + stTimestamp);
            builder.header("traceId", traceId);
            if (tenantId != null) {
                builder.header("tenantId", tenantId);
            }

            // 4. 转发处理和后处理
            ServerHttpRequest redirectRequest = builder.build();
            return chain.filter(exchange.mutate().request(redirectRequest).build()).then(Mono.fromRunnable(() -> {
                log.info("转发到: {} {}",
                        exchange.getAttribute("org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayPredicateMatchedPathRouteIdAttr").toString(),
                        exchange.getAttribute("org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayRequestUrl").toString()
                );
                Long beginTime = exchange.getAttribute("beginTime");
                if (beginTime != null) {
                    log.info("请求处理结束，耗时: {}ms", System.currentTimeMillis() - beginTime);
                }
                MDC.clear();
            }));
        } else {
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
