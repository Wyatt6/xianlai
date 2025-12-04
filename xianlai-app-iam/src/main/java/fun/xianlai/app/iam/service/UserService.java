package fun.xianlai.app.iam.service;

import fun.xianlai.app.iam.model.entity.other.Profile;
import fun.xianlai.app.iam.model.entity.rbac.User;
import fun.xianlai.app.iam.model.form.UserCondition;
import fun.xianlai.app.iam.model.entity.other.UserInfo;
import fun.xianlai.core.response.DataMap;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
     * @param active   状态
     * @return 用户对象
     */
    DataMap createUser(String username, String password, Boolean active);

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

    /**
     * 条件查询用户分页
     * 查询条件为空时查询全量数据
     * 页码<0或页大小<=0时不分页
     *
     * @param pageNum   页码
     * @param pageSize  页大小
     * @param condition 查询条件
     * @return 用户分页数据
     */
    Page<UserInfo> getUserInfoPageConditionally(int pageNum, int pageSize, UserCondition condition);

    /**
     * 根据用户ID查询用户
     */
    Optional<User> findByUserId(Long userId);

    /**
     * 绑定（绑定userId和roleIds）
     *
     * @param userId  用户ID
     * @param roleIds 该用户要绑定的角色ID
     * @return 绑定失败的角色ID列表
     */
    List<Long> bind(Long userId, List<Long> roleIds);

    /**
     * 解除绑定（取消绑定userId和roleIds）
     *
     * @param userId  用户ID
     * @param roleIds 该用户要解除绑定的角色ID列表
     * @return 解除绑定失败的角色ID列表
     */
    List<Long> cancelBind(Long userId, List<Long> roleIds);

    /**
     * 修改用户信息
     */
    DataMap editUserInfo(UserInfo form);

    /**
     * 获取用户的Profile信息
     */
    Profile exportProfile(Long userId);

    /**
     * 上传头像
     */
    void uploadAvatar(MultipartFile avatar);

    /**
     * 下载头像
     */
    void downloadAvatar(String filename, HttpServletResponse response);

}
