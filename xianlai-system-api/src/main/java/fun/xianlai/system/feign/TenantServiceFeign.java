package fun.xianlai.system.feign;

import fun.xianlai.system.dto.XLTenantDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author WyattLau
 */
@FeignClient(
        contextId = "tenant-service-feign",
        name = "xianlai-system-service",
        path = "/tenant"
)
public interface TenantServiceFeign {
    /**
     * 根据ID获取租户
     */
    @GetMapping("/getTenantById")
    XLTenantDTO getTenantById(@RequestParam Long id);
}
