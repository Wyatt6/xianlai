package fun.xianlai.system.service.core.controller;

import fun.xianlai.system.service.core.service.OptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/option")
public class OptionController {
    @Autowired
    private OptionService optionService;
//
//    @ApiLog("修改参数")
//    @SaCheckLogin
//    @SaCheckPermission("option:edit")
//    @PostMapping("/edit")
//    public RetResult edit(@RequestBody SysOption input) {
//        log.info("请求参数: {}", input);
//        BeanUtils.trimString(input);
//        return new RetResult().success().setData(optionService.edit(input));
//    }

//    @ApiLog("重载参数缓存")
//    @SaCheckLogin
//    @SaCheckPermission("option:edit")
//    @GetMapping("/reloadCache")
//    public RetResult reloadCache() {
//        optionService.updateAllFrontLoadOptionsCache();
//        optionService.updateAllBackLoadOptionsCache();
//        return new RetResult().success();
//    }
}
