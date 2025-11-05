package fun.xianlai.app.common.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import fun.xianlai.app.common.service.OptionService;
import fun.xianlai.core.annotation.ApiLog;
import fun.xianlai.core.response.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @ApiLog("获取分类后的参数列表")
    @SaCheckLogin
    @SaCheckPermission("option:query")
    @GetMapping("/getClassifiedList")
    public RetResult getClassifiedList() {
        return new RetResult().success().setData(optionService.getClassifiedList());
    }
}
