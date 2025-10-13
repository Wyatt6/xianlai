package fun.xianlai.app.iam.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import fun.xianlai.app.iam.model.entity.rbac.Permission;
import fun.xianlai.app.iam.model.form.PermissionCondition;
import fun.xianlai.app.iam.model.form.PermissionForm;
import fun.xianlai.app.iam.service.PermissionService;
import fun.xianlai.basic.annotation.ControllerLog;
import fun.xianlai.basic.support.RetResult;
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

    /**
     * 条件查询权限分页
     * 查询条件为空时查询全量数据
     *
     * @param pageNum   页码
     * @param pageSize  页大小
     * @param condition 查询条件
     * @return {pageNum 页码, pageSize 页大小, totalPages 页码总数, totalElements 总条数, permissions 权限分页数据}
     */
    @ControllerLog("条件查询权限分页")
    @SaCheckLogin
    @SaCheckPermission("permission:query")
    @PostMapping("/getPermissionsByPage")
    public RetResult getPermissionsByPage(@RequestParam int pageNum,
                                          @RequestParam int pageSize,
                                          @RequestBody(required = false) PermissionCondition condition) {
        log.info("请求参数: pageNum=[{}], pageSize=[{}], condition=[{}]", pageNum, pageSize, condition);
        Page<Permission> permissions = permissionService.getPermissionsByPageConditionally(
                pageNum,
                pageSize,
                (condition == null || condition.getId() == null) ? null : condition.getId(),
                (condition == null || condition.getIdentifier() == null) ? null : condition.getIdentifier(),
                (condition == null || condition.getName() == null) ? null : condition.getName());
        return new RetResult().success()
                .addData("pageNum", permissions.getPageable().getPageNumber())
                .addData("pageSize", permissions.getPageable().getPageSize())
                .addData("totalPages", permissions.getTotalPages())
                .addData("totalElements", permissions.getTotalElements())
                .addData("permissions", permissions.getContent());
    }

    /**
     * 删除权限
     *
     * @param permissionId 要删除的权限ID
     */
    @ControllerLog("删除权限")
    @SaCheckLogin
    @SaCheckPermission("permission:delete")
    @GetMapping("/deletePermission")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public RetResult deletePermission(@RequestParam Long permissionId) {
        log.info("请求参数: permissionId=[{}]", permissionId);
        permissionService.deletePermission(permissionId);
        return new RetResult().success();
    }

    /**
     * 新增权限
     *
     * @param input 新权限信息
     * @return permission 新权限对象
     */
    @ControllerLog("新增权限")
    @SaCheckLogin
    @SaCheckPermission("permission:add")
    @PostMapping("/addPermission")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public RetResult addPermission(@RequestBody PermissionForm input) {
        log.info("请求参数: {}", input);
        return new RetResult().success().addData("permission", permissionService.createPermission(input.convert()));
    }

    /**
     * 查询权限的排名（从1开始）
     *
     * @param permissionId 权限ID
     */
    @ControllerLog("查询权限的排名（从1开始）")
    @SaCheckLogin
    @SaCheckPermission("permission:query")
    @GetMapping("/getRowNumStartFrom1")
    public RetResult getRowNumStartFrom1(@RequestParam Long permissionId) {
        log.info("请求参数: permissionId=[{}]", permissionId);
        return new RetResult().success().addData("rowNum", permissionService.getRowNum(permissionId));
    }

    /**
     * 修改权限
     *
     * @param input 要修改的权限数据
     * @return permission 新权限对象
     */
    @ControllerLog("修改权限")
    @SaCheckLogin
    @SaCheckPermission("permission:edit")
    @PostMapping("/editPermission")
    public RetResult editPermission(@RequestBody PermissionForm input) {
        log.info("请求参数: {}", input);
        return new RetResult().success().addData("permission", permissionService.updatePermission(input.convert()));
    }
}
