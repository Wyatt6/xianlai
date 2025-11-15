package fun.xianlai.app.common.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import fun.xianlai.app.common.model.entity.SysOption;
import fun.xianlai.app.common.service.OptionService;
import fun.xianlai.core.annotation.ApiLog;
import fun.xianlai.core.response.RetResult;
import fun.xianlai.core.utils.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @ApiLog("新增参数")
    @SaCheckLogin
    @SaCheckPermission("option:add")
    @PostMapping("/add")
    public RetResult add(@RequestBody SysOption form) {
        log.info("请求参数: {}", form);
        BeanUtils.trimString(form);
        return new RetResult().success().setData(optionService.add(form));
    }

    @ApiLog("删除参数")
    @SaCheckLogin
    @SaCheckPermission("option:delete")
    @GetMapping("/delete")
    public RetResult delete(@RequestParam Long optionId) {
        log.info("请求参数: optionId=[{}]", optionId);
        optionService.delete(optionId);
        return new RetResult().success();
    }

    @ApiLog("修改参数")
    @SaCheckLogin
    @SaCheckPermission("option:edit")
    @PostMapping("/edit")
    public RetResult edit(@RequestBody SysOption form) {
        log.info("请求参数: {}", form);
        BeanUtils.trimString(form);
        return new RetResult().success().setData(optionService.edit(form));
    }

    @ApiLog("重载参数缓存")
    @SaCheckLogin
    @SaCheckPermission("option:edit")
    @GetMapping("/reloadCache")
    public RetResult reloadCache() {
        optionService.cacheFrontLoadOptions();
        optionService.cacheBackLoadOptions();
        return new RetResult().success();
    }

    @ApiLog("获取分类后的参数列表")
    @SaCheckLogin
    @SaCheckPermission("option:query")
    @GetMapping("/getClassifiedList")
    public RetResult getClassifiedList() {
        return new RetResult().success().setData(optionService.getClassifiedList());
    }
}
