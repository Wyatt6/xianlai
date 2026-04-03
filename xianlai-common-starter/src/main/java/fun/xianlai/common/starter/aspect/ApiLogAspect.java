package fun.xianlai.common.starter.aspect;

import com.alibaba.fastjson2.JSONObject;
import fun.xianlai.common.annotation.ApiLog;
import fun.xianlai.common.context.RequestContext;
import fun.xianlai.common.utils.text.LogFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 基于Spring的AOP机制定义的接口自动日志打印和响应数据封装操作，用于使用@ApiLog注解的Controller方法
 * Spring AOP框架定义的各种Advice执行顺序：
 * order注解越小越优先
 * Around(前处理部份) --> Before --> 目标方法 --> After --> AfterReturning / AfterThrowing --> Around(后处理部份)
 * 如果有异常最后还会抛出给ExceptionHandler
 *
 * @author WyattLau
 */
@Slf4j
@Aspect
@Component
@Order(2)
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
        boolean isInner = false;
        String fromService = RequestContext.getFromService();
        if (fromService != null && !fromService.isBlank()) {
            if (!"gateway".equals(fromService)) {
                isInner = true;
            }
        }

        // 获取@ApiLog的值（API名称）
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();   // 获取正在处理的方法对象
        ApiLog annotation = method.getAnnotation(ApiLog.class);                     // 获取对该方法@ApiLog注解的对象
        if (annotation == null) {
            annotation = joinPoint.getTarget().getClass().getAnnotation(ApiLog.class);
        }
        String annotationValue = annotation.value();    // 获取@ApiLog注解的值
        log.info("API Name      : 【{}】", annotationValue);
        log.info("API Target    : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        if (!isInner) {
            log.info("Params        : {}", LogFormatUtils.paramLoggingFormatter(joinPoint.getArgs()));  // TODO 敏感数据脱敏
        }
        log.info("Execution Begin......");

        Object result = joinPoint.proceed();
        if (!isInner) {
            log.info("Result        : \n{}", getResultPrintText(result));
        }
        return result;
    }

    /**
     * 返回的数据可能很多，不能写入到日志中占用太多的I/O资源，特别是并发场景下
     * 只打印前200字符，超长截断
     */
    private String getResultPrintText(Object result) {
        String jsonString = JSONObject.toJSONString(result);
        return jsonString.length() > 200 ? jsonString.substring(0, 200) + " ... (total length: " + jsonString.length() + ")" : jsonString;
    }
}
