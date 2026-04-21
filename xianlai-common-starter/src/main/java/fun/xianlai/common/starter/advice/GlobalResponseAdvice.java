package fun.xianlai.common.starter.advice;

import fun.xianlai.common.constant.ApiConst;
import fun.xianlai.common.constant.HeaderConst;
import fun.xianlai.common.constant.MenuConst;
import fun.xianlai.common.constant.PathConst;
import fun.xianlai.common.constant.RouteConst;
import fun.xianlai.common.constant.TenantConst;
import fun.xianlai.common.context.RequestContext;
import fun.xianlai.common.response.RetResult;
import fun.xianlai.common.starter.service.ConfigService;
import fun.xianlai.common.utils.bean.BeanUtils;
import fun.xianlai.common.utils.time.DateUtils;
import fun.xianlai.system.dto.XLTenantDTO;
import fun.xianlai.system.feign.TenantServiceFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author WyattLau
 */
@Slf4j
@RestControllerAdvice
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {
    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private ConfigService configService;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;    // 对所有请求都生效
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
            result.setBeginTime(RequestContext.getBeginTime());
            result.setFinishTime(DateUtils.nowMilliTimestamp());
            response.getHeaders().add(HeaderConst.GLOBAL_CONFIG_VERSION, "" + configService.getLocalGlobalVersion());
            response.getHeaders().add(HeaderConst.TENANT_CONFIG_VERSION, "" + configService.getTenantConfigVersionFromCache(RequestContext.getTenantId()));
            response.getHeaders().add(HeaderConst.PATH_UPDATE_TIME, this.getUpdateTimeInSystemConfig(PathConst.UPDATE_TIME_CONFIG_KEY));
            response.getHeaders().add(HeaderConst.ROUTE_UPDATE_TIME, this.getUpdateTimeInSystemConfig(RouteConst.UPDATE_TIME_CONFIG_KEY));
            response.getHeaders().add(HeaderConst.MENU_UPDATE_TIME, this.getUpdateTimeInSystemConfig(MenuConst.UPDATE_TIME_CONFIG_KEY));
            response.getHeaders().add(HeaderConst.API_UPDATE_TIME, this.getUpdateTimeInSystemConfig(ApiConst.UPDATE_TIME_CONFIG_KEY));
            return result;
        } else {
            return body;
        }
    }

    private String getUpdateTimeInSystemConfig(String key) {
//        if (!redis.hasKey(ConfigConst.CONFIG_CACHE_KEY)) {
//            log.info("缓存找不到系统配置，先重新缓存");
////            configServiceFeign.cacheSystemConfigs();
//        }
//        Map<String, Object> configMap = BeanUtils.objectToMap(redis.opsForHash().get(ConfigConst.CONFIG_CACHE_KEY, key));
//        if (configMap.isEmpty()) {
        return "0";
//        } else {
//            LocalDateTime configUpdateTime = DateUtils.parseMilliDateTime((String) configMap.get("value"));
//            return "" + DateUtils.localDateTimeToMilliTimestamp(configUpdateTime);
//        }
    }
}
