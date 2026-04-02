package fun.xianlai.gateway.filter;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jwt.JWTClaimsSet;
import fun.xianlai.common.constant.HeaderConst;
import fun.xianlai.common.utils.time.DateUtils;
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
public class LogAndRedirectFilter implements GlobalFilter, Ordered {
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
            // 当前时间戳
            long beginTime = DateUtils.nowMilliTimestamp();
            exchange.getAttributes().put("beginTime", beginTime);

            // 生成traceId并打印开始日志
            String traceId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
            MDC.put("traceId", traceId);
            log.info("==================== 开始请求 ====================");

            // 打印当前时间
            log.info("Begin Time    : {}", DateUtils.timeMilliFormat(DateUtils.milliTimstampToLocalDateTime(beginTime)));

            // 打印请求基础信息
            String requestTimeStr = request.getHeaders().getFirst(HeaderConst.REQUEST_TIME);
            if (requestTimeStr == null || requestTimeStr.isBlank()) {
                log.info("Request Time  : [NONE]");
            } else {
                long requestTimestamp = Long.parseLong(requestTimeStr);
                log.info("Request Time  : {}", DateUtils.timeMilliFormat(DateUtils.milliTimstampToLocalDateTime(requestTimestamp)));
            }
            // TODO 打印请求IP
            log.info("Request Method: {}", request.getMethod());
            log.info("Request URL   : {}", request.getURI());

            // 解析报文头token获取tenantId（尚未验签）
            String token = request.getHeaders().getFirst(HeaderConst.TOKEN);
            String tenantId = null;
            if (token == null || token.isBlank()) {
                log.info("Token         : [NONE]");
            } else {
                try {
                    JWSObject jwsObject = JWSObject.parse(token);
                    JWTClaimsSet claims = JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());
                    tenantId = claims.getStringClaim("tenantId");
                    log.info("Tenant ID     : {}", tenantId);
                } catch (Exception e) {
                    log.warn(e.getMessage());
                }
            }

            // 3. 组装转发请求的报文头
            ServerHttpRequest.Builder builder = request.mutate();
            builder.header(HeaderConst.BEGIN_TIME, "" + beginTime);
            builder.header(HeaderConst.FROM_SERVICE, "gateway");
            builder.header(HeaderConst.TRACE_ID, traceId);
            if (tenantId != null) {
                builder.header(HeaderConst.TENANT_ID, tenantId);
            }

            // 4. 转发处理和后处理
            ServerHttpRequest redirectRequest = builder.build();
            return chain.filter(exchange.mutate().request(redirectRequest).build()).then(Mono.fromRunnable(() -> {
                log.info("转发到: {} {}",
                        exchange.getAttribute("org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayPredicateMatchedPathRouteIdAttr").toString(),
                        exchange.getAttribute("org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayRequestUrl").toString()
                );
                Long beginTimeCached = exchange.getAttribute("beginTime");
                long finishTime = DateUtils.nowMilliTimestamp();
                if (beginTimeCached != null) {
                    log.info("Time Cost     : {}ms", finishTime - beginTimeCached);
                }
                log.info("Finish Time   : {}", DateUtils.timeMilliFormat(DateUtils.milliTimstampToLocalDateTime(finishTime)));
                log.info("==================== 完成请求 ====================");
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
