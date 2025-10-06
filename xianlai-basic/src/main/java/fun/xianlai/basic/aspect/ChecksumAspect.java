package fun.xianlai.basic.aspect;

import fun.xianlai.basic.support.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author WyattLau
 */
@Slf4j
@Aspect
@Component
@Order(2)
public class ChecksumAspect {
    @Autowired
    private RedisTemplate<String, Object> redis;

    @Pointcut("execution(* fun.xianlai..*.controller..*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object doAroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        RetResult result = (RetResult) joinPoint.proceed();

        log.info("checksum");
        return result;
    }

    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public void assembleChecksum(JoinPoint joinPoint, RetResult result) {
        String sysOptionsChecksum = (String) redis.opsForValue().get("sysOptionsChecksum");
        if (sysOptionsChecksum != null) {
            result.addChecksum("sysOptionsChecksum", sysOptionsChecksum);
        }
    }
}
