package fun.xianlai.app.iam.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import fun.xianlai.app.iam.model.entity.rbac.Role;
import fun.xianlai.app.iam.model.form.RoleCondition;
import fun.xianlai.app.iam.model.form.RoleForm;
import fun.xianlai.app.iam.service.RoleService;
import fun.xianlai.basic.annotation.ControllerLog;
import fun.xianlai.basic.support.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
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
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 条件查询角色分页
     *
     * @param pageNum   页码
     * @param pageSize  页大小
     * @param condition 查询条件
     * @return {pageNum 页码, pageSize 页大小, totalPages 页码总数, totalElements 总条数, roles 角色分页}
     */
    @ControllerLog("条件查询角色分页")
    @SaCheckLogin
    @SaCheckPermission("role:query")
    @PostMapping("/getRolesByPage")
    public RetResult getRolesByPage(@RequestParam int pageNum,
                                    @RequestParam int pageSize,
                                    @RequestBody(required = false) RoleCondition condition) {
        log.info("请求参数：pageNum=[{}], pageSize=[{}], condition=[{}]", pageNum, pageSize, condition);
        Page<Role> roles = roleService.getRolesByPageConditionally(
                pageNum,
                pageSize,
                (condition == null || condition.getIdentifier() == null) ? null : condition.getIdentifier(),
                (condition == null || condition.getName() == null) ? null : condition.getName(),
                (condition == null || condition.getActive() == null) ? null : condition.getActive(),
                (condition == null || condition.getPermission() == null) ? null : condition.getPermission());
        return new RetResult().success()
                .addData("pageNum", roles.getPageable().getPageNumber())
                .addData("pageSize", roles.getPageable().getPageSize())
                .addData("totalPages", roles.getTotalPages())
                .addData("totalElements", roles.getTotalElements())
                .addData("roles", roles.getContent());
    }

    /**
     * 新增角色
     *
     * @param input 新角色信息
     * @return role 新角色对象
     */
    @ControllerLog("新增角色")
    @SaCheckLogin
    @SaCheckPermission("role:add")
    @PostMapping("/addRole")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public RetResult addRole(@RequestBody RoleForm input) {
        log.info("请求参数: {}", input);
        Role role = roleService.createRole(input.convert());
        return new RetResult().success().addData("role", role);
    }

    /**
     * 查询角色的排名（从1开始）
     *
     * @param roleId 角色ID
     */
    @ControllerLog("查询角色的排名（从1开始）")
    @SaCheckLogin
    @SaCheckPermission("role:query")
    @GetMapping("/getRowNumStartFrom1")
    public RetResult getRowNumStartFrom1(@RequestParam Long roleId) {
        log.info("请求参数: roleId=[{}]", roleId);
        return new RetResult().success().addData("rowNum", roleService.getRowNum(roleId));
    }

    /**
     * 删除角色
     *
     * @param roleId 要删除的角色ID
     */
    @ControllerLog("删除角色")
    @SaCheckLogin
    @SaCheckPermission("role:delete")
    @GetMapping("/deleteRole")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public RetResult deleteRole(@RequestParam Long roleId) {
        log.info("请求参数: roleId=[{}]", roleId);
        roleService.deleteRole(roleId);
        return new RetResult().success();
    }

    /**
     * 修改角色
     *
     * @param input 要修改的角色数据
     * @return role 新角色对象
     */
    @ControllerLog("修改角色")
    @SaCheckLogin
    @SaCheckPermission("role:edit")
    @PostMapping("/editRole")
    public RetResult editRole(@RequestBody RoleForm input) {
        log.info("请求参数: {}", input);
        return new RetResult().success().addData("role", roleService.updateRole(input.convert()));
    }
}
