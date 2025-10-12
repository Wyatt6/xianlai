package fun.xianlai.app.iam.service;

import java.util.Date;
import java.util.List;

/**
 * @author WyattLau
 */
public interface RoleService {
    /**
     * 从缓存或数据库获取用户生效中的角色标识符
     *
     * @param userId 用户ID
     * @return 角色标识符列表
     */
    List<String> getActiveRoleIdentifiers(Long userId);

    /**
     * 设置rolesCacheOfUserRefreshed时间戳
     */
    void setRolesCacheOfUserRefreshed(Long userId, Date timestamp);
}
