package fun.xianlai.app.iam.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * 请求拦截与日志打印
 * <p>
 * 对于从服务网关转发过来的请求，从报文头获取服务网关生成的traceId并保存到MDC中，
 * 前端直接发送过来不经过服务网关的请求，生成traceId并保存到MDC中，
 * 使得traceId可以在其他地方被获取。同时打印该请求的开始分界线日志信息用以区分。
 *
 * @author WyattLau
 */
@Slf4j
@Component
public class RequestLogFilter extends OncePerRequestFilter implements Filter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String url = request.getRequestURL().toString();
            if (!url.matches(".*/actuator.*")) {
                // 从服务网关报文头获取traceId或者生成traceId
                String traceId = request.getHeader("traceId");
                if (traceId == null) {
                    traceId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
                }
                // traceId保存在日志框架的MDC上下文中，方便其他地方获取使用
                MDC.put("traceId", traceId);
                log.info(">>>>> 请求: {} {}", request.getMethod(), request.getRequestURL());
            }
            // 处理请求
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
