package fun.xianlai.app.iam.service;


import fun.xianlai.app.iam.model.entity.rbac.Permission;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * @author WyattLau
 */
public interface PermissionService {
//    /**
//     * 创建权限对象
//     *
//     * @param permission 新权限信息
//     * @return 新权限对象
//     */
//    Permission createPermission(Permission permission);
//
//    /**
//     * 删除权限对象
//     *
//     * @param permissionId 要删除的权限ID
//     */
//    void deletePermission(Long permissionId);
//
//    /**
//     * 更新权限对象
//     *
//     * @param permission 新权限数据
//     * @return 权限对象
//     */
//    Permission updatePermission(Permission permission);
//
//    /**
//     * 获取全量权限数据
//     *
//     * @return 全量权限数据
//     */
//    List<Permission> listPermissions();
//
//    /**
//     * 获取某角色的权限
//     *
//     * @param roleId 角色ID
//     * @return 某角色的权限ID列表
//     */
//    List<Long> getPermissionIdsOfRole(Long roleId);
//
    /**
     * 条件查询权限分页
     *
     * @param pageNum    页码
     * @param pageSize   页大小
     * @param id         模块ID
     * @param identifier 权限标识（模糊）
     * @param name       权限名称（模糊）
     * @return 权限分页数据
     */
    Page<Permission> getPermissionsByPageConditionally(int pageNum, int pageSize, Long id, String identifier, String name);

//    /**
//     * 查询某权限的排名
//     *
//     * @param permissionId 权限ID
//     * @return 排名（从1开始）
//     */
//    Long getRowNum(Long permissionId);

    /**
     * 从缓存或数据库获取用户生效中的权限标识符
     *
     * @param userId 用户ID
     * @return 权限标识符列表
     */
    List<String> getActivePermissionIdentifiers(Long userId);

    //    /**
//     * 更新PERMISSIONS_DB_REFRESHED时间戳
//     */
//    void setPermissionsDbRefreshed(Date timestamp);

    /**
     * 设置permissionsCacheOfUserRefreshed时间戳
     */
    void setPermissionsCacheOfUserRefreshed(Long userId, Date timestamp);
}
