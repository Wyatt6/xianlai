package fun.xianlai.common.starter.aspect;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.common.annotation.ApiLog;
import fun.xianlai.common.utils.text.LogFormatUtils;
import fun.xianlai.common.utils.time.DateUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * 基于Spring的AOP机制定义的接口自动日志打印和响应数据封装操作，用于使用@ApiLog注解的Controller方法
 * Spring AOP框架定义的各种Advice执行顺序：
 * Around(前处理部份) --> Before --> 目标方法 --> After --> AfterReturning / AfterThrowing --> Around(后处理部份)
 * 如果有异常最后还会抛出给ExceptionHandler
 *
 * @author WyattLau
 */
@Slf4j
@Aspect
@Component
public class ApiLogAspect {
    @Pointcut("@annotation(fun.xianlai.common.annotation.ApiLog)")
    public void pointcut() {
    }

    /**
     * 围绕Controller方法前后进行的操作
     * 包括打印分界线日志和辅助日志、封装响应数据
     *
     * @param joinPoint 切点
     * @return Controller方法return的值
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTimestamp = DateUtils.nowMilliTimestamp();

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (attributes != null) ? attributes.getRequest() : null;

        // 获取@ApiLog的值（API名称）
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();   // 获取正在处理的方法对象
        ApiLog annotation = method.getAnnotation(ApiLog.class);                     // 获取对该方法@ApiLog注解的对象
        if (annotation == null) {
            annotation = joinPoint.getTarget().getClass().getAnnotation(ApiLog.class);
        }
        String annotationValue = annotation.value();    // 获取@ApiLog注解的值

        log.info("==================== Controller 开始 ====================");
        if (request != null) {
            log.info("URL        : {}", request.getRequestURL());
            log.info("HTTP Method: {}", request.getMethod());
            log.info("IP         : {}", getRemoteIp(request));
        } else {
            log.info("No Request Context");
        }
        log.info("API Name   : {}", annotationValue);
        log.info("Target     : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        log.info("Params     : {}", LogFormatUtils.paramLoggingFormatter(joinPoint.getArgs()));  // TODO 敏感数据脱敏
        log.info("Execution -->");

        try {
            Object result = joinPoint.proceed();
            log.info("Result     : {}", getResultPrintText(result));
            log.info("Time Cost  : {}ms", DateUtils.nowMilliTimestamp() - startTimestamp);
            log.info("==================== Controller 结束 ====================");
            return result;
        } catch (Throwable e) {
            log.info("Exception  : {} {}", e.getMessage(), e.getClass().getName());
            log.info("Time Cost  : {}ms", DateUtils.nowMilliTimestamp() - startTimestamp);
            log.info("=============== Controller 结束（执行异常） ===============");
            throw e;    // 原样抛出异常给ExceptionHandler进行处理
        }
    }

    /**
     * 返回的数据可能很多，不能写入到日志中占用太多的I/O资源，特别是并发场景下
     * 只打印前200字符，超长截断
     */
    private String getResultPrintText(Object result) {
        String jsonString = JSONObject.toJSONString(result);
        return jsonString.length() > 200 ? jsonString.substring(0, 200) + " ... (total length: " + jsonString.length() + ")" : jsonString;
    }

    /**
     * 获取真实IP（处理Nginx、代理转发的报文）
     * Nginx配置要加上：
     * proxy_set_header Host $host;
     * proxy_set_header X-Real-IP $remote_addr;
     * proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
     */
    private String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");                   // 国际标准，必须配置
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("X-Real-IP");                            // Nginx专用，常用
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("Proxy-Client-IP");                      // Apache专用
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("WL-Proxy-Client-IP");                   // WebLogic专用
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
            ip = request.getRemoteAddr();                                       // 兜底
        return ip;
    }
}
