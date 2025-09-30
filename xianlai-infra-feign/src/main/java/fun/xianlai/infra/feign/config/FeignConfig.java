package fun.xianlai.infra.feign.config;

import com.alibaba.fastjson2.JSON;
import feign.FeignException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import fun.xianlai.basic.support.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author WyattLau
 */
@Slf4j
@Configuration
public class FeignConfig {
    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public RequestInterceptor feignRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                requestTemplate.header("traceId", MDC.get("traceId"));
            }
        };
    }

    @Bean
    public Decoder feignResponseDecoder() {
        return new OptionalDecoder(new ResponseEntityDecoder(new Decoder() {
            @Override
            public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
                String bodyStr = Util.toString(response.body().asReader(Util.UTF_8));
                RetResult retResult = JSON.parseObject(bodyStr, RetResult.class);
                if (retResult.getSuccess()) {
                    // FastJSON优先将不越界的数字解析成Integer，Integer无法隐式转换成Long，需要手工解析
                    if (long.class.getTypeName().equalsIgnoreCase(type.getTypeName())) {
                        return Long.parseLong(retResult.readFeignData().toString());
                    }
                    if (Long.class.getTypeName().equalsIgnoreCase(type.getTypeName())) {
                        return Long.parseLong(retResult.readFeignData().toString());
                    }
                    return retResult.readFeignData();
                } else {
                    throw retResult.readFeignSysException();
                }
            }
        }));
    }
}
