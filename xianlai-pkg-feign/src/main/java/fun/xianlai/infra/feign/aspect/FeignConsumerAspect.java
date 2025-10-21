package fun.xianlai.infra.feign.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author WyattLau
 */
@Slf4j
@Aspect
@Component
public class FeignConsumerAspect {
    @Pointcut("execution(* fun.xianlai..*.feign.consumer..*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object doAroundFeignService(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTimestamp = System.currentTimeMillis();
        log.info(">>>>>> Feign Consumer [{}] in [{}]", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringTypeName());
        try {
            Object result = joinPoint.proceed();
            log.info("调用结果: {}", result);
            log.info("调用耗时: {}ms", System.currentTimeMillis() - startTimestamp);
            log.info("<<<<<< Exit Feign Consumer [{}]", joinPoint.getSignature().getName());
            return result;
        } catch (Throwable e) {
            // Feign会把异常封装到DecodeException里，原异常就保存在cause里
            log.info("出现异常: {} {}", e.getCause().getMessage(), e.getCause().getClass().getName());
            log.info("调用耗时: {}ms", System.currentTimeMillis() - startTimestamp);
            log.info("<<<<<< Exit Feign Consumer [{}] with Exception", joinPoint.getSignature().getName());
            throw e.getCause(); // 需要继续向调用该Service的上层Service或Controller抛出异常，不能拦截在这里形成无返回的情况
        }
    }
}
