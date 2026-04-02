package fun.xianlai.system.core.controller;

import fun.xianlai.common.response.RetResult;
import fun.xianlai.system.core.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WyattLau
 */
@RestController
@RequestMapping("/config")
public class ConfigController {
    @Autowired
    ConfigService configService;

    @GetMapping("/cacheSystemConfigs")
    public RetResult<?> cacheSystemConfigs() {
        configService.cacheSystemConfigs();
        return RetResult.success();
    }
}
