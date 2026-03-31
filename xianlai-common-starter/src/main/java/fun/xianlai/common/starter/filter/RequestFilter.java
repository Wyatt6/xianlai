package fun.xianlai.common.starter.filter;

import fun.xianlai.common.constant.HeaderConst;
import fun.xianlai.common.context.RequestContext;
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
                // 1. 获取网关传递的traceId，并保存在RequestContext和日志框架的MDC上下文中
                String traceId = request.getHeader(HeaderConst.TRACE_ID);
                if (traceId == null) {
                    traceId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
                }
                RequestContext.setTraceId(traceId);
                MDC.put("traceId", traceId);
                // 2. 获取网关传递的stTimestamp，并保存在RequestContext中
                String stTimestampStr = request.getHeader(HeaderConst.ST_TIMESTAMP);
                if (stTimestampStr != null && !stTimestampStr.isBlank()) {
                    RequestContext.setStTimestamp(Long.parseLong(stTimestampStr));
                }
                // 3. 获取网关传递的tenantId，并保存在TenantContext中
                log.info("****** 请求: {} {}", request.getMethod(), request.getRequestURL());
                String tenantIdStr = request.getHeader(HeaderConst.TENANT_ID);
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
            RequestContext.clear();
            TenantContext.clear();
        }
    }
}
