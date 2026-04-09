package fun.xianlai.common.starter.advice;

import fun.xianlai.common.constant.HeaderConst;
import fun.xianlai.common.constant.PathConst;
import fun.xianlai.common.constant.RouteConst;
import fun.xianlai.common.constant.SystemConst;
import fun.xianlai.common.constant.TenantConst;
import fun.xianlai.common.context.RequestContext;
import fun.xianlai.common.response.RetResult;
import fun.xianlai.common.utils.bean.BeanUtils;
import fun.xianlai.common.utils.time.DateUtils;
import fun.xianlai.system.dto.XLTenantDTO;
import fun.xianlai.system.feign.ConfigServiceFeign;
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
    private ConfigServiceFeign configServiceFeign;
    @Autowired
    private TenantServiceFeign tenantServiceFeign;

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
            response.getHeaders().add(HeaderConst.SYSTEM_CONFIG_UPDATE_TIME, this.getSystemConfigUpdateTime());
            response.getHeaders().add(HeaderConst.TENANT_CONFIG_UPDATE_TIME, this.getTenantConfigUpdateTime(RequestContext.getTenantId()));
            response.getHeaders().add(HeaderConst.PATH_UPDATE_TIME, this.getPathUpdateTime());
            response.getHeaders().add(HeaderConst.ROUTE_UPDATE_TIME, this.getRouteUpdateTime());
            return result;
        } else {
            return body;
        }
    }

    private String getSystemConfigUpdateTime() {
        if (!redis.hasKey(SystemConst.CONFIG_CACHE_KEY)) {
            log.info("缓存找不到系统配置，先重新缓存");
            configServiceFeign.cacheSystemConfigs();
        }
        Map<String, Object> configMap = BeanUtils.objectToMap(redis.opsForHash().get(SystemConst.CONFIG_CACHE_KEY, SystemConst.CONFIG_UPDATE_TIME_CONFIG_KEY));
        if (configMap.isEmpty()) {
            return "0";
        } else {
            LocalDateTime configUpdateTime = DateUtils.parseMilliDateTime((String) configMap.get("value"));
            return "" + DateUtils.localDateTimeToMilliTimestamp(configUpdateTime);
        }
    }

    private String getTenantConfigUpdateTime(Long tenantId) {
        if (tenantId == null) return "0";
        String key = MessageFormat.format(TenantConst.ENTITY_CACHE_KEY, tenantId);
        if (redis.hasKey(key)) {
            Map<String, Object> tenantMap = BeanUtils.objectToMap(redis.opsForValue().get(key));
            return "" + DateUtils.localDateTimeToMilliTimestamp((LocalDateTime) tenantMap.get("configUpdateTime"));
        } else {
            log.info("缓存找不到租户数据");
            XLTenantDTO tenant = tenantServiceFeign.getTenantById(tenantId);
            return tenant == null ? "0" : "" + DateUtils.localDateTimeToMilliTimestamp(tenant.getConfigUpdateTime());
        }
    }

    private String getPathUpdateTime() {
        if (!redis.hasKey(SystemConst.CONFIG_CACHE_KEY)) {
            log.info("缓存找不到系统配置，先重新缓存");
            configServiceFeign.cacheSystemConfigs();
        }
        Map<String, Object> configMap = BeanUtils.objectToMap(redis.opsForHash().get(SystemConst.CONFIG_CACHE_KEY, PathConst.UPDATE_TIME_CONFIG_KEY));
        if (configMap.isEmpty()) {
            return "0";
        } else {
            LocalDateTime configUpdateTime = DateUtils.parseMilliDateTime((String) configMap.get("value"));
            return "" + DateUtils.localDateTimeToMilliTimestamp(configUpdateTime);
        }
    }

    private String getRouteUpdateTime() {
        if (!redis.hasKey(SystemConst.CONFIG_CACHE_KEY)) {
            log.info("缓存找不到系统配置，先重新缓存");
            configServiceFeign.cacheSystemConfigs();
        }
        Map<String, Object> configMap = BeanUtils.objectToMap(redis.opsForHash().get(SystemConst.CONFIG_CACHE_KEY, RouteConst.UPDATE_TIME_CONFIG_KEY));
        if (configMap.isEmpty()) {
            return "0";
        } else {
            LocalDateTime configUpdateTime = DateUtils.parseMilliDateTime((String) configMap.get("value"));
            return "" + DateUtils.localDateTimeToMilliTimestamp(configUpdateTime);
        }
    }
}
