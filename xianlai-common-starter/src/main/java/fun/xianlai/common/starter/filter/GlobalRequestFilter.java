package fun.xianlai.common.starter.filter;

import fun.xianlai.common.constant.HeaderConst;
import fun.xianlai.common.context.RequestContext;
import fun.xianlai.common.utils.net.IpUtils;
import fun.xianlai.common.utils.time.DateUtils;
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
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author WyattLau
 */
@Slf4j
@Component
public class GlobalRequestFilter extends OncePerRequestFilter implements Filter {
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
        boolean isHandle = false;
        boolean isInner = false;
        LocalDateTime enterTime = DateUtils.now();
        try {
            String path = request.getRequestURI();
            isHandle = shouldHandle(path);
            if (isHandle) {
                // traceId
                String traceId = request.getHeader(HeaderConst.TRACE_ID);
                if (traceId == null || traceId.isBlank()) {
                    traceId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
                    RequestContext.setTraceId(traceId);
                    MDC.put("traceId", traceId);
                    log.info("报文头 {} 不存在或为空，使用自生成的 traceId，不影响程序运行但链路跟踪可能出现异常", HeaderConst.TRACE_ID);
                } else {
                    RequestContext.setTraceId(traceId);
                    MDC.put("traceId", traceId);
                }

                // fromService
                String fromService = request.getHeader(HeaderConst.FROM_SERVICE);
                if (fromService != null && !fromService.isBlank()) {
                    RequestContext.setFromService(fromService);
                    if (!"gateway".equals(fromService)) {
                        isInner = true;
                    }
                }

                log.info("==================== {} ====================", isInner ? "开始（内部）请求" : "开始请求");
                log.info("Enter Time    : {}", DateUtils.timeMilliFormat(enterTime));
                if (fromService == null || fromService.isBlank()) {
                    log.info("From Service  : 报文头 {} 不存在或为空，不影响程序运行但无法确定上游服务", HeaderConst.FROM_SERVICE);
                } else {
                    log.info("From Service  : {}", fromService);
                }
                if (!isInner) {
                    // beginTime
                    String beginTimeStr = request.getHeader(HeaderConst.BEGIN_TIME);
                    if (beginTimeStr != null && !beginTimeStr.isBlank()) {
                        long beginTime = Long.parseLong(beginTimeStr);
                        RequestContext.setBeginTime(beginTime);
                        log.info("Begin Time    : {}", DateUtils.timeMilliFormat(DateUtils.milliTimstampToLocalDateTime(beginTime)));
                    } else {
                        log.info("Begin Time    : 报文头 {} 不存在，不影响程序运行但响应的 RetResult 对象的 beginTime 为默认值 0", HeaderConst.BEGIN_TIME);
                    }
                    log.info("Request IP    : {}", IpUtils.getRemoteIp(request));
                    log.info("Request Method: {}", request.getMethod());
                    log.info("Request URL   : {}", request.getRequestURL());
                }
                // TODO 打印脱敏token
                // tenantId
                String tenantIdStr = request.getHeader(HeaderConst.TENANT_ID);
                if (tenantIdStr != null && !tenantIdStr.isBlank()) {
                    RequestContext.setTenantId(Long.parseLong(tenantIdStr));
                    log.info("Tenant ID     : {}", tenantIdStr);
                } else {
                    log.warn("Tenant ID     : 报文头 {} 不存在或为空，可能影响程序运行", tenantIdStr);
                }
            }
            // 处理请求
            filterChain.doFilter(request, response);
        } finally {
            if (isHandle) {
                LocalDateTime exitTime = DateUtils.now();
                log.info("Time Cost     : {}ms", DateUtils.betweenMillis(enterTime, exitTime));
                log.info("Exit Time     : {}", DateUtils.timeMilliFormat(exitTime));
                log.info("==================== {} ====================", isInner ? "完成（内部）请求" : "完成请求");
            }
            MDC.clear();
            RequestContext.clear();
        }
    }
}
