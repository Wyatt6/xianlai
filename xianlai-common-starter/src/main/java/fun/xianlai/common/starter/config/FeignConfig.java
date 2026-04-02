package fun.xianlai.common.starter.config;

import com.alibaba.fastjson2.JSON;
import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.Util;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import fun.xianlai.common.constant.HeaderConst;
import fun.xianlai.common.context.RequestContext;
import fun.xianlai.common.exception.BizException;
import fun.xianlai.common.exception.SysException;
import fun.xianlai.common.response.RetCode;
import fun.xianlai.common.response.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.lang.reflect.Type;

/**
 * @author WyattLau
 */
@Slf4j
@Configuration
@ConditionalOnClass(FeignAutoConfiguration.class)   // 这样之后引入Feign依赖时这个配置类才会生效
public class FeignConfig {
    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;
    @Autowired
    private Environment env;

    @Bean
    public Feign.Builder feignBuilder(Decoder decoder) {
        // 对于声明为void的方法也依旧执行decode过程，从RetResult中提取可能返回的异常
        return Feign.builder().decoder(decoder).decodeVoid();
    }

    @Bean
    public RequestInterceptor feignRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                template.header(HeaderConst.FROM_SERVICE, env.getProperty("spring.application.name"));

                String token = RequestContext.getToken();
                if (token != null && !token.isBlank()) {
                    template.header(HeaderConst.TOKEN, token);
                }

                Long tenantId = RequestContext.getTenantId();
                if (tenantId != null) {
                    template.header(HeaderConst.TENANT_ID, "" + tenantId);
                }

                String traceId = RequestContext.getTraceId();
                if (traceId != null && !traceId.isBlank()) {
                    template.header(HeaderConst.TRACE_ID, traceId);
                }
            }
        };
    }

    @Bean
    public Decoder feignResponseDecoder() {
        return new OptionalDecoder(new ResponseEntityDecoder(new Decoder() {
            /**
             * @param response  所调用的返回值，应是一个RetResult对象
             * @param type      要把返回值解析成的类型，如void、int、Long等
             */
            @Override
            public Object decode(Response response, Type type) {
                RetResult<?> retResult;

                try {
                    String bodyStr = Util.toString(response.body().asReader(Util.UTF_8));
                    retResult = JSON.parseObject(bodyStr, RetResult.class);
                } catch (Exception e) {
                    throw new SysException(RetCode.FEIGN_ERROR, "Feign响应解析失败");
                }

                if (retResult == null) {
                    throw new SysException(RetCode.FEIGN_ERROR, "Feign调用响应为空");
                }

                if (retResult.getSuccess()) {
                    if (type == Void.class || type == Void.TYPE) {
                        return null;        // 如果目标类型是void，返回null
                    }
                    if (type == RetResult.class) {
                        return retResult;   // 如果目标类型就是RetResult，直接返回
                    }
                    Object data = retResult.getData();
                    return JSON.parseObject(JSON.toJSONString(data), type);
                } else {
                    String code = retResult.getCode();
                    String message = retResult.getMessage();
                    if (RetCode.BIZ_ERROR.equals(code.substring(0, 3))) {
                        throw new BizException(code, message);
                    }
                    if (RetCode.SYS_ERROR.equals(code.substring(0, 3))) {
                        throw new SysException(code, message);
                    }
                    throw new SysException(code, "Feign调用产生未知异常: " + message);
                }
            }
        }));
    }
}
