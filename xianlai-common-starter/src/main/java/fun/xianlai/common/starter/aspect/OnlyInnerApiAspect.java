package fun.xianlai.common.starter.aspect;

import fun.xianlai.common.annotation.OnlyInnerApi;
import fun.xianlai.common.context.RequestContext;
import fun.xianlai.common.exception.BizException;
import fun.xianlai.common.response.RetCode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author WyattLau
 */
@Slf4j
@Aspect
@Component
@Order(1)
public class OnlyInnerApiAspect {
    @Before("@annotation(onlyInnerApi)")
    public void check(OnlyInnerApi onlyInnerApi) {
        boolean isInner = false;     // 安全起见，无法确认的情况下认为是外部接口，禁止调用
        String fromService = RequestContext.getFromService();
        if (fromService != null && !fromService.isBlank()) {
            if (!"gateway".equals(fromService)) {
                isInner = true;
            }
        }
        if (!isInner) {
            throw new BizException(RetCode.FORBIDDEN, "内部接口，禁止外部访问");
        }
    }
}
