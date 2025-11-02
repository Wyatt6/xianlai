package fun.xianlai.app.iam.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import fun.xianlai.app.iam.model.entity.rbac.Permission;
import fun.xianlai.app.iam.model.form.PermissionCondition;
import fun.xianlai.app.iam.model.form.PermissionForm;
import fun.xianlai.app.iam.service.PermissionService;
import fun.xianlai.core.annotation.ApiLog;
import fun.xianlai.core.response.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public RetResult add(@RequestBody PermissionForm form) {
        log.info("请求参数: {}", form);
        return new RetResult().success().setData(permissionService.add(form.convert()));
    }

    @ApiLog("删除权限")
    @SaCheckLogin
    @SaCheckPermission("permission:delete")
    @GetMapping("/delete")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public RetResult delete(@RequestParam Long permissionId) {
        log.info("请求参数: permissionId=[{}]", permissionId);
        permissionService.deletePermission(permissionId);
        return new RetResult().success();
    }

    @ApiLog("修改权限")
    @SaCheckLogin
    @SaCheckPermission("permission:edit")
    @PostMapping("/edit")
    public RetResult edit(@RequestBody PermissionForm form) {
        log.info("请求参数: {}", form);
        return new RetResult().success().addData("permission", permissionService.updatePermission(form.convert()));
    }

    /**
     * 条件查询权限分页
     * 查询条件为空时查询全量数据
     * 页码<0或页大小<=0时不分页
     *
     * @param pageNum   页码（从0开始）
     * @param pageSize  页大小
     * @param condition 查询条件
     * @return {pageNum 页码, pageSize 页大小, totalPages 页码总数, totalElements 总条数, content 分页数据}
     */
    @ApiLog("条件查询权限分页")
    @SaCheckLogin
    @SaCheckPermission("permission:query")
    @PostMapping("/getPageConditionally")
    public RetResult getPageConditionally(@RequestParam int pageNum,
                                          @RequestParam int pageSize,
                                          @RequestBody(required = false) PermissionCondition condition) {
        log.info("请求参数: pageNum=[{}], pageSize=[{}], condition=[{}]", pageNum, pageSize, condition);
        Page<Permission> permissions = permissionService.getByPageConditionally(pageNum, pageSize, condition);
        return new RetResult().success()
                .addData("pageNum", pageNum)
                .addData("pageSize", pageSize)
                .addData("totalPages", permissions.getTotalPages())
                .addData("totalElements", permissions.getTotalElements())
                .addData("content", permissions.getContent());
    }

    @ApiLog("查询某角色所拥有的权限ID列表")
    @SaCheckLogin
    @SaCheckPermission("permission:query")
    @GetMapping("/getPermissionIdsOfRole")
    public RetResult getPermissionIdsOfRole(@RequestParam Long roleId) {
        log.info("请求参数: roleId=[{}]", roleId);
        return new RetResult().success().addData("permissionIds", permissionService.getPermissionIdsOfRole(roleId));
    }
}
