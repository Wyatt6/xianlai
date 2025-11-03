package fun.xianlai.app.iam.service;

import fun.xianlai.app.iam.model.entity.rbac.Role;
import fun.xianlai.core.response.DataMap;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * @author WyattLau
 */
public interface RoleService {
    /**
     * 创建角色
     */
    DataMap add(Role role);

    /**
     * 删除角色
     */
    void deleteRole(Long roleId);

    /**
     * 更新角色
     */
    DataMap edit(Role role);

    /**
     * 查询条件为空时查询全量数据
     * 页码<0或页大小<=0时不分页
     *
     * @param pageNum   页码
     * @param pageSize  页大小
     * @param condition 查询条件
     * @return 分页数据
     */
    Page<Role> getRolesByPageConditionally(int pageNum, int pageSize, Role condition);

    /**
     * 获取某用户的角色ID列表
     */
    List<Long> getRoleIdsOfUser(Long userId);

    /**
     * 设置roleDbRefreshTime时间戳
     */
    void setRoleDbRefreshTime(Date timestamp);

    /**
     * 授权（绑定roleId和permissionIds）
     *
     * @param roleId        角色ID
     * @param permissionIds 该角色要绑定的权限ID
     * @return 授权失败的权限ID列表
     */
    List<Long> grant(Long roleId, List<Long> permissionIds);

    /**
     * 解除授权（取消绑定roleId和permissionIds）
     *
     * @param roleId        角色ID
     * @param permissionIds 该角色要解除绑定的权限ID列表
     * @return 解除授权失败的权限ID列表
     */
    List<Long> cancelGrant(Long roleId, List<Long> permissionIds);
}
