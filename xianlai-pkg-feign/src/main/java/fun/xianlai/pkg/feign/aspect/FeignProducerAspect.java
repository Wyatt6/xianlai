package fun.xianlai.pkg.feign.aspect;

import com.alibaba.fastjson2.JSON;
import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.support.RetResult;
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
    public Object doAroundFeignController(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTimestamp = System.currentTimeMillis();
        log.info(">>> Feign Producer [{}] in [{}] ", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringTypeName());
        try {
            Object result = joinPoint.proceed();
            log.info("处理耗时: {}ms", System.currentTimeMillis() - startTimestamp);
            log.info("<<< Finish Feign Producer [{}]", joinPoint.getSignature().getName());
            return result;
        } catch (Throwable e) {
            if (e instanceof SysException) {
                log.info("处理耗时: {}ms", System.currentTimeMillis() - startTimestamp);
                log.info("<<< Finish Feign Producer [{}] with SysException", joinPoint.getSignature().getName());
                return new RetResult().writeFeignSysException((SysException) e).setTraceId(MDC.get("traceId"));
            } else {
                log.info("处理耗时: {}ms", System.currentTimeMillis() - startTimestamp);
                log.info("<<< Finish Feign Producer [{}] with Unknown Exception", joinPoint.getSignature().getName());
                throw e;
            }
        }
    }

    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public void afterReturningFeignController(JoinPoint joinPoint, RetResult result) {
        result.setTraceId(MDC.get("traceId"));
    }
}
