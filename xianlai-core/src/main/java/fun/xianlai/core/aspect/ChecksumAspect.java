package fun.xianlai.core.aspect;

import fun.xianlai.core.response.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
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

    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public void assembleChecksum(JoinPoint joinPoint, RetResult result) {
        String sysOptionsChecksum = (String) redis.opsForValue().get("sysOptionsChecksum");
        if (sysOptionsChecksum != null) {
            result.addChecksum("sysOptionsChecksum", sysOptionsChecksum);
        }

        String pathsChecksum = (String) redis.opsForValue().get("pathsChecksum");
        if (pathsChecksum != null) {
            result.addChecksum("pathsChecksum", pathsChecksum);
        }

        String sysMenusChecksum = (String) redis.opsForValue().get("sysMenusChecksum");
        if (sysMenusChecksum != null) {
            result.addChecksum("sysMenusChecksum", sysMenusChecksum);
        }

        String sysApisChecksum = (String) redis.opsForValue().get("sysApisChecksum");
        if (sysApisChecksum != null) {
            result.addChecksum("sysApisChecksum", sysApisChecksum);
        }

        String sysRoutesChecksum = (String) redis.opsForValue().get("sysRoutesChecksum");
        if (sysRoutesChecksum != null) {
            result.addChecksum("sysRoutesChecksum", sysRoutesChecksum);
        }
    }
}
