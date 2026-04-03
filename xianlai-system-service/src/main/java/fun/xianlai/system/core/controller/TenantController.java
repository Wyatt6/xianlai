package fun.xianlai.system.core.controller;

import fun.xianlai.common.response.RetResult;
import fun.xianlai.system.core.convert.XLTenantConverter;
import fun.xianlai.system.core.entity.XLTenant;
import fun.xianlai.system.core.service.TenantService;
import fun.xianlai.system.dto.XLTenantDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WyattLau
 */
@RestController
@RequestMapping("/core/tenant")
public class TenantController {
    @Autowired
    private TenantService tenantService;

    @GetMapping("/getTenantById")
    RetResult<XLTenantDTO> getTenantById(@RequestParam Long id) {
        Assert.notNull(id, "ID不能为空");
        XLTenant tenant = tenantService.getTenantById(id);
        return RetResult.success(XLTenantConverter.INSTANCE.toDto(tenant));
    }
}
