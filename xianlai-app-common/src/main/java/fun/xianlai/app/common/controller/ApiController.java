package fun.xianlai.app.common.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import fun.xianlai.app.common.model.entity.SysApi;
import fun.xianlai.app.common.service.ApiService;
import fun.xianlai.core.annotation.ApiLog;
import fun.xianlai.core.response.RetResult;
import fun.xianlai.core.utils.bean.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private ApiService apiService;

    @ApiLog("新增接口")
    @SaCheckLogin
    @SaCheckPermission("api:add")
    @PostMapping("/add")
    public RetResult add(@RequestBody SysApi form) {
        log.info("请求参数: {}", form);
        BeanUtils.trimString(form);
        return new RetResult().success().setData(apiService.add(form));
    }

    @ApiLog("删除接口")
    @SaCheckLogin
    @SaCheckPermission("api:delete")
    @GetMapping("/delete")
    public RetResult delete(@RequestParam Long apiId) {
        log.info("请求参数: apiId=[{}]", apiId);
        apiService.delete(apiId);
        return new RetResult().success();
    }

    @ApiLog("修改接口")
    @SaCheckLogin
    @SaCheckPermission("api:edit")
    @PostMapping("/edit")
    public RetResult edit(@RequestBody SysApi form) {
        log.info("请求参数: {}", form);
        BeanUtils.trimString(form);
        return new RetResult().success().setData(apiService.edit(form));
    }

    @ApiLog("重载接口缓存")
    @SaCheckLogin
    @SaCheckPermission("api:edit")
    @GetMapping("/reloadCache")
    public RetResult reloadCache() {
        apiService.cacheApis();
        return new RetResult().success();
    }
}
