package fun.xianlai.app.iam.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import fun.xianlai.app.iam.model.entity.rbac.Role;
import fun.xianlai.app.iam.model.form.GrantForm;
import fun.xianlai.app.iam.model.form.RoleCondition;
import fun.xianlai.app.iam.model.form.RoleForm;
import fun.xianlai.app.iam.service.RoleService;
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

import java.util.List;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @ApiLog("新增角色")
    @SaCheckLogin
    @SaCheckPermission("role:add")
    @PostMapping("/add")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public RetResult add(@RequestBody RoleForm form) {
        log.info("请求参数: {}", form);
        return new RetResult().success().addData("role", roleService.createRole(form.convert()));
    }

    @ApiLog("删除角色")
    @SaCheckLogin
    @SaCheckPermission("role:delete")
    @GetMapping("/delete")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public RetResult delete(@RequestParam Long roleId) {
        log.info("请求参数: roleId=[{}]", roleId);
        roleService.deleteRole(roleId);
        return new RetResult().success();
    }

    @ApiLog("修改角色")
    @SaCheckLogin
    @SaCheckPermission("role:edit")
    @PostMapping("/edit")
    public RetResult edit(@RequestBody RoleForm form) {
        log.info("请求参数: {}", form);
        return new RetResult().success().addData("role", roleService.updateRole(form.convert()));
    }

    /**
     * 条件查询角色分页
     * 查询条件为空时查询全量数据
     * 页码<0或页大小<=0时不分页
     *
     * @param pageNum   页码
     * @param pageSize  页大小
     * @param condition 查询条件
     * @return {pageNum 页码, pageSize 页大小, totalPages 页码总数, totalElements 总条数, content 分页数据}
     */
    @ApiLog("条件查询角色分页")
    @SaCheckLogin
    @SaCheckPermission("role:query")
    @PostMapping("/getPageConditionally")
    public RetResult getPageConditionally(@RequestParam int pageNum,
                                          @RequestParam int pageSize,
                                          @RequestBody(required = false) RoleCondition condition) {
        log.info("请求参数：pageNum=[{}], pageSize=[{}], condition=[{}]", pageNum, pageSize, condition);
        Page<Role> roles = roleService.getRolesByPageConditionally(pageNum, pageSize, condition);
        return new RetResult().success()
                .addData("pageNum", pageNum)
                .addData("pageSize", pageSize)
                .addData("totalPages", roles.getTotalPages())
                .addData("totalElements", roles.getTotalElements())
                .addData("content", roles.getContent());
    }

    @ApiLog("查询角色的排名（从1开始）")
    @SaCheckLogin
    @SaCheckPermission("role:query")
    @GetMapping("/getRowNumStartFrom1")
    public RetResult getRowNumStartFrom1(@RequestParam Long roleId) {
        log.info("请求参数: roleId=[{}]", roleId);
        return new RetResult().success().addData("rowNum", roleService.getRowNum(roleId));
    }

    @ApiLog("查询某用户所具有的角色ID列表")
    @SaCheckLogin
    @SaCheckPermission("role:query")
    @GetMapping("/getRoleIdsOfUser")
    public RetResult getRoleIdsOfUser(@RequestParam Long userId) {
        log.info("请求参数: userId=[{}]", userId);
        return new RetResult().success().addData("roleIds", roleService.getRoleIdsOfUser(userId));
    }

    @ApiLog("为角色授权/解除授权")
    @SaCheckLogin
    @SaCheckPermission("role:bind")
    @PostMapping("/grants")
    public RetResult grants(@RequestBody GrantForm form) {
        log.info("请求参数: {}", form);
        List<Long> failGrant = null;
        List<Long> failCancel = null;
        try {
            log.info("授权");
            failGrant = roleService.grant(form.getRoleId(), form.getGrant());
        } catch (IllegalArgumentException e) {
            log.info("无须授权");
        }
        try {
            log.info("解除授权");
            failCancel = roleService.cancelGrant(form.getRoleId(), form.getCancel());
        } catch (IllegalArgumentException e) {
            log.info("无须解除授权");
        }
        return new RetResult().success()
                .addData("failGrant", failGrant)
                .addData("failCancel", failCancel);
    }
}
