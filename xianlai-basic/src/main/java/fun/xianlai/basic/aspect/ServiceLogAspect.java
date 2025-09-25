package fun.xianlai.basic.aspect;

import fun.xianlai.basic.annotation.ServiceLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author WyattLau
 */
@Slf4j
@Aspect
@Component
public class ServiceLogAspect {
    @Pointcut("@annotation(fun.xianlai.basic.annotation.ServiceLog)")
    public void pointcut() {
    }

    /**
     * 围绕Service方法前后打印分界线日志
     *
     * @param joinPoint 切点
     * @return 方法return的值
     */
    @Around("pointcut()")
    public Object doAroundService(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();   // 获取正在处理的方法对象
        ServiceLog annotation = method.getAnnotation(ServiceLog.class);             // 获取对该方法@ServiceLog注解的对象
        if (annotation == null) {
            annotation = joinPoint.getTarget().getClass().getAnnotation(ServiceLog.class);
        }
        String annotationValue = annotation.value();    // 获取@ServiceLog注解的值

        log.info(">>>>>> Call Service {}[{}] in [{}]", annotationValue, joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringTypeName());
        try {
            Object serviceReturn = joinPoint.proceed();
            log.info("<<<<<< Exit Service {}[{}]", annotationValue, joinPoint.getSignature().getName());
            return serviceReturn;
        } catch (Throwable e) {
            log.info("<<<<<< Exit Service {}[{}] with Exception", annotationValue, joinPoint.getSignature().getName());
            throw e;    // 需要继续向调用该Service的上层Service或Controller抛出异常，不能拦截在这里形成无返回的情况
        }
    }

    /**
     * Service方法不能正常执行完毕，有异常抛出，打印到日志中
     *
     * @param joinPoint 切点
     * @param e         Service抛出的异常
     */
    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception e) {
        log.info("出现异常: {} {}", e.getMessage(), e.getClass().getName());
    }
}
