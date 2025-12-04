package fun.xianlai.app.iam.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import fun.xianlai.app.iam.model.entity.rbac.Permission;
import fun.xianlai.app.iam.service.PermissionService;
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
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @ApiLog("新增权限")
    @SaCheckLogin
    @SaCheckPermission("permission:add")
    @PostMapping("/add")
    public RetResult add(@RequestBody Permission form) {
        log.info("请求参数: {}", form);
        BeanUtils.trimString(form);
        return new RetResult().success().setData(permissionService.add(form));
    }

    @ApiLog("删除权限")
    @SaCheckLogin
    @SaCheckPermission("permission:delete")
    @GetMapping("/delete")
    public RetResult delete(@RequestParam Long permissionId) {
        log.info("请求参数: permissionId=[{}]", permissionId);
        permissionService.delete(permissionId);
        return new RetResult().success();
    }

    @ApiLog("修改权限")
    @SaCheckLogin
    @SaCheckPermission("permission:edit")
    @PostMapping("/edit")
    public RetResult edit(@RequestBody Permission form) {
        log.info("请求参数: {}", form);
        BeanUtils.trimString(form);
        return new RetResult().success().setData(permissionService.edit(form));
    }

}
