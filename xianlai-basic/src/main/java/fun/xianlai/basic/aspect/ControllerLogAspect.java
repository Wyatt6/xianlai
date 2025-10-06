package fun.xianlai.basic.aspect;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.basic.annotation.ControllerLog;
import fun.xianlai.basic.exception.SysException;
import fun.xianlai.basic.support.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 基于Spring的AOP机制定义的Controller自动日志打印和响应数据封装操作，
 * 用于使用@ControllerLog注解的Controller方法
 * <p>
 * Spring AOP框架定义的各种Advice执行顺序：
 * Around(前处理部份) --> Before --> 目标方法 --> After --> AfterReturning / AfterThrowing --> Around(后处理部份)
 *
 * @author WyattLau
 */
@Slf4j
@Aspect
@Component
@Order(1)
public class ControllerLogAspect {
    @Pointcut("@annotation(fun.xianlai.basic.annotation.ControllerLog)")
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
    public Object doAroundController(ProceedingJoinPoint joinPoint) {
        Long startTimestamp = System.currentTimeMillis();

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();   // 获取正在处理的方法对象
        ControllerLog annotation = method.getAnnotation(ControllerLog.class);       // 获取对该方法@ControllerLog注解的对象
        if (annotation == null) {
            annotation = joinPoint.getTarget().getClass().getAnnotation(ControllerLog.class);
        }
        String annotationValue = annotation.value();    // 获取@ControllerLog注解的值

        log.info(">>> Run  Controller {}[{}] in [{}] ", annotationValue, joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringTypeName());
        try {
            Object result = joinPoint.proceed();
            log.info("处理耗时: {}ms", System.currentTimeMillis() - startTimestamp);
            log.info("<<< Exit Controller {}[{}]", annotationValue, joinPoint.getSignature().getName());
            return result;
        } catch (Throwable e) {
            // 当Controller不能正常执行完毕抛出异常时，封装成失败的响应信息
            RetResult result = new RetResult();
            if (e instanceof SysException) {
                result.setFailMessage(e.getMessage());
            } else if (e instanceof IllegalArgumentException) {
                result.setFailCode("400").setFailMessage("请求参数错误");
            } else {
                result.setFailCode("500").setFailMessage("服务器内部错误");
            }
            result.fail().setTraceId(MDC.get("traceId"));
            logResponseText(result);
            log.info("处理耗时: {}ms", System.currentTimeMillis() - startTimestamp);
            log.info("<<< Exit Controller {}[{}] with Exception", annotationValue, joinPoint.getSignature().getName());
            return result;
        }
    }

    /**
     * Controller方法正常执行完毕
     * 从MDC中获取traceId装配到响应对象中，并打印响应数据到日志中（响应数据由@Around进行返回）
     *
     * @param joinPoint 切点
     * @param result    Controller方法的返回（即响应对象）
     */
    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public void controllerFinished(JoinPoint joinPoint, RetResult result) {
        result.setTraceId(MDC.get("traceId"));
        logResponseText(result);
    }

    /**
     * Controller方法不能正常执行完毕，有异常抛出
     * 打印异常信息到日志中（异常情况的响应数据由@Around进行封装和返回）
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void controllerFinishedWithException(JoinPoint joinPoint, Exception e) {
        log.info("错误: {} {}", e.getMessage(), e.getClass().getName());
    }

    /**
     * 返回的数据可能很多，此时就不能写入到日志中占用太多的I/O资源，
     * 根据RESPONSE_MAX_LENGTH常量判断响应数据是否超出最大长度
     *
     * @param response 响应对象
     */
    private void logResponseText(RetResult response) {
        String jsonString = JSONObject.toJSONString(response);
        if (jsonString.length() > 5000) {
            log.info("返回数据超长，不写入日志占用I/O资源");
        } else {
            log.info("返回: {}", jsonString);
        }
    }
}
