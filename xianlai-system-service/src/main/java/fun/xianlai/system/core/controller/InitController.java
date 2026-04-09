package fun.xianlai.system.core.controller;

import fun.xianlai.common.annotation.ApiLog;
import fun.xianlai.common.response.RetResult;
import fun.xianlai.system.core.entity.XLTenant;
import fun.xianlai.system.core.service.ConfigService;
import fun.xianlai.system.core.service.TenantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/core/init")
public class InitController {
    @Autowired
    private TenantService tenantService;
    @Autowired
    private ConfigService configService;

    @ApiLog("获取初始化数据")
    @GetMapping("/getInitData")
    public RetResult<?> getInitData(@RequestParam String domain) {
        XLTenant tenant = tenantService.getTenantByDomain(domain);
        Map<String, Map<String, Object>> configs = configService.getTenantFrontLoadConfigs(tenant.getId());

        Map<String, Object> model = new HashMap<>();
        model.put("tenant", tenant);
        model.put("configs", configs);

        return RetResult.success(model);
    }
}
