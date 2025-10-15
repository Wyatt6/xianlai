package fun.xianlai.app.iam.service;

import fun.xianlai.app.iam.model.entity.rbac.Role;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * @author WyattLau
 */
public interface RoleService {
    /**
     * 创建角色
     *
     * @param role 新角色信息
     * @return 新角色对象
     */
    Role createRole(Role role);

    /**
     * 更新角色对象
     *
     * @param role 角色新数据（id非空）
     * @return 角色的新对象
     */
    Role updateRole(Role role);

    /**
     * 删除角色对象
     *
     * @param roleId 要删除的角色ID
     */
    void deleteRole(Long roleId);

    /**
     * 条件查询角色分页
     *
     * @param pageNum    页码
     * @param pageSize   页大小
     * @param identifier 角色标识（模糊）
     * @param name       角色名称（模糊）
     * @param active     生效状态
     * @param permission 角色所包含的权限
     * @return 角色分页
     */
    Page<Role> getRolesByPageConditionally(
            int pageNum,
            int pageSize,
            String identifier,
            String name,
            Boolean active,
            String permission);

    /**
     * 查询某角色的排名
     *
     * @param roleId 角色ID
     * @return 排名（从1开始）
     */
    Long getRowNum(Long roleId);

    /**
     * 从缓存或数据库获取用户生效中的角色标识符
     *
     * @param userId 用户ID
     * @return 角色标识符列表
     */
    List<String> getActiveRoleIdentifiers(Long userId);

    /**
     * 设置rolesDbRefreshed时间戳
     */
    void setRolesDbRefreshed(Date timestamp);

    /**
     * 设置rolesCacheOfUserRefreshed时间戳
     */
    void setRolesCacheOfUserRefreshed(Long userId, Date timestamp);
}
