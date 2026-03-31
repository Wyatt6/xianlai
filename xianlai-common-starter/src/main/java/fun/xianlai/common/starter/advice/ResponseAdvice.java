package fun.xianlai.common.starter.advice;

import fun.xianlai.common.context.RequestContext;
import fun.xianlai.common.response.RetResult;
import fun.xianlai.common.utils.time.DateUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author WyattLau
 */
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 对所有请求都生效
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if (body instanceof RetResult<?> result) {
            result.setTraceId(RequestContext.getTraceId());
            result.setStTimestamp(RequestContext.getStTimestamp());
            result.setEdTimestamp(DateUtils.nowMilliTimestamp());
            // TODO configUpdateTime
            return result;
        } else {
            return body;
        }
    }
}
