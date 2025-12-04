package fun.xianlai.app.iam.service;

import fun.xianlai.app.iam.model.entity.rbac.Permission;
import fun.xianlai.core.response.DataMap;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * @author WyattLau
 */
public interface PermissionService {
    /**
     * 新增权限
     */
    DataMap add(Permission permission);

    /**
     * 删除权限
     */
    void delete(Long permissionId);

    /**
     * 修改权限
     */
    DataMap edit(Permission permission);

    /**
     * 查询条件为空时查询全量数据
     * 页码<0或页大小<=0时不分页
     *
     * @param pageNum   页码
     * @param pageSize  页大小
     * @param condition 查询条件
     * @return 分页数据
     */
    Page<Permission> getByPageConditionally(int pageNum, int pageSize, Permission condition);

    /**
     * 获取某角色的权限ID列表
     */
    List<Long> getPermissionIdsOfRole(Long roleId);

    /**
     * 设置permissionDbRefreshTime时间戳
     */
    void setPermissionDbRefreshTime(Date timestamp);
}
