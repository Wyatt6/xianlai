package fun.xianlai.pkg.feign.aspect;

import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.response.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * @author WyattLau
 */
@Slf4j
@Aspect
@Component
public class FeignProducerAspect {
    @Pointcut("execution(* fun.xianlai..*.feign.producer..*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTimestamp = System.currentTimeMillis();
        log.info("-->> Feign Call [{}] in [{}] ", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringTypeName());
        try {
            Object result = joinPoint.proceed();
            log.info("处理耗时: {}ms", System.currentTimeMillis() - startTimestamp);
            log.info("<<-- Finish Feign Call [{}]", joinPoint.getSignature().getName());
            return result;
        } catch (Throwable e) {
            if (e instanceof SysException) {
                log.info("处理耗时: {}ms", System.currentTimeMillis() - startTimestamp);
                log.info("<<-- Finish Feign Call [{}] with SysException", joinPoint.getSignature().getName());
                return new RetResult().writeFeignSysException((SysException) e).setTraceId(MDC.get("traceId"));
            } else {
                log.info("处理耗时: {}ms", System.currentTimeMillis() - startTimestamp);
                log.info("<<-- Finish Feign Call [{}] with Unknown Exception", joinPoint.getSignature().getName());
                throw e;
            }
        }
    }

    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, RetResult result) {
        result.setTraceId(MDC.get("traceId"));
    }
}
