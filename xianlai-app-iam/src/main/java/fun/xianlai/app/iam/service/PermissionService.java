package fun.xianlai.app.iam.service;


import fun.xianlai.app.iam.model.entity.rbac.Permission;
import fun.xianlai.app.iam.model.form.PermissionCondition;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * @author WyattLau
 */
public interface PermissionService {
    /**
     * 创建权限
     */
    Permission createPermission(Permission permission);

    /**
     * 删除权限
     */
    void deletePermission(Long permissionId);

    /**
     * 更新权限
     */
    Permission updatePermission(Permission permission);

    /**
     * 条件查询权限分页
     * 查询条件为空时查询全量数据
     * 页码<0或页大小<=0时不分页
     *
     * @param pageNum   页码
     * @param pageSize  页大小
     * @param condition 查询条件
     * @return 权限分页数据
     */
    Page<Permission> getPermissionsByPageConditionally(int pageNum, int pageSize, PermissionCondition condition);

    /**
     * 设置permissionDbRefreshTime时间戳
     */
    void setPermissionDbRefreshTime(Date timestamp);

    /**
     * 查询某权限的排名（从1开始）
     */
    Long getRowNum(Long permissionId);

    /**
     * 获取某角色的权限ID列表
     */
    List<Long> getPermissionIdsOfRole(Long roleId);
}
