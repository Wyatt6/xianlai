package fun.xianlai.common.filter;

import fun.xianlai.common.context.TenantContext;
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
 * @author WyattLau
 */
@Slf4j
@Component
public class RequestFilter extends OncePerRequestFilter implements Filter {

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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String path = request.getRequestURI();
            if (shouldHandle(path)) {
                // 1. 获取网关传递的traceId，并保存在日志框架的MDC上下文中
                String traceId = request.getHeader("traceId");
                if (traceId == null) {
                    traceId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
                }
                MDC.put("traceId", traceId);
                log.info("****** 请求: {} {}", request.getMethod(), request.getRequestURL());
                // 2. 获取网关传递的tenantId，并保存在租户上下文中
                String tenantIdStr = request.getHeader("tenantId");
                if (tenantIdStr != null && !tenantIdStr.isBlank()) {
                    Long tenantId = Long.parseLong(tenantIdStr);
                    TenantContext.setTenantId(tenantId);
                    log.info("tenantId: {}", tenantId);
                } else {
                    log.info("报文头无tenantId");
                }
            }
            // 处理请求
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
            TenantContext.clear();
        }
    }
}
