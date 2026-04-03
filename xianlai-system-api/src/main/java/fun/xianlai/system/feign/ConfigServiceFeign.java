package fun.xianlai.system.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author WyattLau
 */
@FeignClient(
        contextId = "config-service-feign",
        name = "xianlai-system-service",
        path = "/core/config"
)
public interface ConfigServiceFeign {
    /**
     * 缓存系统配置
     */
    @GetMapping("/cacheSystemConfigs")
    void cacheSystemConfigs();
}
