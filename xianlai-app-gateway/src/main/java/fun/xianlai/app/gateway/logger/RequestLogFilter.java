package fun.xianlai.app.gateway.logger;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * 打印每个请求的分界线
 *
 * @author WyattLau
 */
@Slf4j
@Component
public class RequestLogFilter implements WebFilter {
    private static final String[] NOT_PRINT_URL_PATTERN_REGEX = {
            ".*/actuator.*"
    };

    private boolean shouldPrint(String url) {
        for (String regex : NOT_PRINT_URL_PATTERN_REGEX) {
            if (url.matches(regex)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 处理请求
        return chain.filter(exchange)
                .contextWrite(context -> {
                    ServerHttpRequest request = exchange.getRequest();
                    String url = request.getURI().toString();
                    if (shouldPrint(url)) {
                        String traceId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
                        MDC.put("traceId", traceId);
                        log.info(">>>>> 开始处理请求: {} {}", request.getMethod(), request.getURI());
                    }
                    return context;
                });
    }
}
