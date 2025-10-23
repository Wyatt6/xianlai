package fun.xianlai.app.iam.service;

import fun.xianlai.app.iam.model.entity.rbac.User;

import java.util.Date;
import java.util.List;

/**
 * @author WyattLau
 */
public interface UserService {
    boolean matchUsernameFormat(String username);

    boolean matchPasswordFormat(String password);

    /**
     * 创建用户
     *
     * @param username 用户名
     * @param password 密码（明文）
     * @return 用户对象
     */
    User createUser(String username, String password);

    /**
     * 身份验证（用户名+密码）
     * 若验证成功则返回用户对象
     *
     * @param username 用户名
     * @param password 密码（明文）
     * @return 用户对象
     */
    User authentication(String username, String password);

    /**
     * 身份验证（用户ID+密码）
     * 若验证成功则返回用户对象
     *
     * @param userId   用户ID
     * @param password 密码（明文）
     * @return 用户对象
     */
    User authentication(Long userId, String password);

    /**
     * 从缓存或数据库获取用户角色
     *
     * @param userId 用户ID
     * @return 生效中的角色标识符列表
     */
    List<String> getRoleList(Long userId);

    /**
     * 从缓存或数据库获取用户权限标
     *
     * @param userId 用户ID
     * @return 生效中的权限标识符列表
     */
    List<String> getPermissionList(Long userId);

    /**
     * 设置roleListCacheTime时间戳
     */
    void setRoleListCacheTime(Long userId, Date timestamp);

    /**
     * 设置permissionListCacheTime时间戳
     */
    void setPermissionListCacheTime(Long userId, Date timestamp);

    /**
     * 修改密码
     */
    void changePassword(Long id, String password);
}
