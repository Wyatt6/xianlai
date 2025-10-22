package fun.xianlai.core.aspect;

import fun.xianlai.core.annotation.SimpleServiceLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 基于Spring的AOP机制定义的Service自动日志打印，
 * 针对极简单的服务，不用打印分界线等多余信息，只需打印服务名
 *
 * @author WyattLau
 */
@Slf4j
@Aspect
@Component
public class SimpleServiceLogAspect {
    @Pointcut("@annotation(fun.xianlai.core.annotation.SimpleServiceLog)")
    public void pointcut() {
    }

    /**
     * Service方法正常执行完毕
     *
     * @param joinPoint 切点
     * @return 方法return的值
     */
    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public Object afterReturning(JoinPoint joinPoint, Object result) throws Throwable {
        String annotationValue = this.getAnnotationValue(joinPoint);
        if ("void".equals(((MethodSignature) joinPoint.getSignature()).getMethod().getReturnType().getName())) {
            log.info("调用服务[{} {}]成功", annotationValue, joinPoint.getSignature().getName());
        } else {
            log.info("调用服务[{} {}]成功，返回值: {}", annotationValue, joinPoint.getSignature().getName(), result);
        }
        return result;
    }

    /**
     * Service方法不能正常执行完毕，有异常抛出，打印到日志中并往上层抛出异常
     *
     * @param joinPoint 切点
     * @param e         Service抛出的异常
     */
    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) throws Throwable {
        String annotationValue = this.getAnnotationValue(joinPoint);
        log.info("调用服务[{} {}]异常: {} {}", annotationValue, joinPoint.getSignature().getName(), e.getMessage(), e.getClass().getName());
        throw e;    // 需要继续向调用该Service的上层Service或Controller抛出异常，不能拦截在这里形成无返回的情况
    }

    private String getAnnotationValue(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();       // 获取正在处理的方法对象
        SimpleServiceLog annotation = method.getAnnotation(SimpleServiceLog.class);     // 获取对该方法@SimpleServiceLog注解的对象
        if (annotation == null) {
            annotation = joinPoint.getTarget().getClass().getAnnotation(SimpleServiceLog.class);
        }
        return annotation.value();    // 获取@SimpleServiceLog注解的值
    }
}
