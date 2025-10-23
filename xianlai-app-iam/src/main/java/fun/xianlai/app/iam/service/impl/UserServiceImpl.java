package fun.xianlai.app.iam.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import fun.xianlai.app.iam.model.entity.rbac.Permission;
import fun.xianlai.app.iam.model.entity.rbac.Role;
import fun.xianlai.app.iam.model.entity.rbac.User;
import fun.xianlai.app.iam.repository.PermissionRepository;
import fun.xianlai.app.iam.repository.RoleRepository;
import fun.xianlai.app.iam.repository.UserRepository;
import fun.xianlai.app.iam.service.UserService;
import fun.xianlai.core.annotation.ServiceLog;
import fun.xianlai.core.annotation.SimpleServiceLog;
import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.feign.consumer.FeignOptionService;
import fun.xianlai.core.utils.DateUtil;
import fun.xianlai.core.utils.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private FeignOptionService optionService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    @SimpleServiceLog("检查用户名格式")
    public boolean matchUsernameFormat(String username) {
        String USERNAME_REGEXP = optionService.readValueInString("user.username.regexp").orElse("^[a-zA-Z][a-zA-Z_0-9]{4,19}$");
        return username.matches(USERNAME_REGEXP);
    }

    @Override
    @SimpleServiceLog("检查密码格式")
    public boolean matchPasswordFormat(String password) {
        String PASSWORD_REGEXP = optionService.readValueInString("user.password.regexp").orElse("^[a-zA-Z_0-9.~!@#$%^&*?]{6,30}$");
        return password.matches(PASSWORD_REGEXP);
    }

    @Override
    @ServiceLog("创建新用户")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public User createUser(String username, String password) {
        log.info("检查用户名是否已被使用");
        if (userRepository.findByUsername(username) != null) {
            throw new SysException("用户名已被使用");
        }

        log.info("密码加密");
        String salt = PasswordUtil.generateSalt();
        String encryptedPassword = PasswordUtil.encode(password, salt);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(encryptedPassword);
        newUser.setSalt(salt);
        newUser.setRegisterTime(DateUtil.now());
        newUser.setActive(true);
        User savedUser = userRepository.save(newUser);
        log.info("成功创建新用户: id=[{}]", savedUser.getId());

        return savedUser;
    }

    @Override
    @SimpleServiceLog("身份验证")
    public User authentication(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(PasswordUtil.encode(password, user.getSalt()))) {
            throw new SysException("用户名或密码错误");
        }
        return user;
    }

    /**
     * 公共标记：roleDbRefreshTime   数据库的角色数据更新的时间戳
     * 用户标记：roleListCacheTime   本用户角色数据缓存更新时间戳
     * <p>
     * 一、由于角色的数据库变更造成的需要大范围用户刷新角色缓存的场景
     * 1、角色记录的identifier、active变更时，更新roleDbRefreshTime；其他字段变更不打紧。
     * 2、若roleListCacheTime >= roleDbRefreshTime表明缓存中的角色数据已经是最新的，先查询缓存的角色数据作为结果，查不到缓存再查数据库更新缓存。
     * 3、若不满足上述条件则查数据库更新缓存，刷新缓存后更新roleListCacheTime。
     * <p>
     * 二、由于用户自身变更造成的自己需要刷新角色缓存的场景
     * 1、用户自身变更时，置用户标记roleListCacheTime为0。
     * 2、若roleListCacheTime >= roleDbRefreshTime表明缓存中的角色数据已经是最新的，则先查询缓存的角色数据作为结果，查不到缓存再查数据库更新缓存。
     * 3、若不满足上述条件则查数据库更新缓存，刷新缓存后更新roleListCacheTime。
     */
    @Override
    public List<String> getRoleList(Long userId) {
        List<String> roles = null;
        SaSession session = StpUtil.getSessionByLoginId(userId);

        Date t1 = (Date) redis.opsForValue().get("roleDbRefreshTime");
        Date t2 = (Date) session.get("roleListCacheTime");
        // 先查询Session缓存的角色数据
        if (t1 == null || (t2 != null && t2.compareTo(t1) >= 0)) {
            // 第一二点第2小点：返回Session缓存的角色数据
            roles = (List<String>) session.get("roleList");
        }
        if (roles == null) {
            // 第一二点第3小点：先用数据库数据刷新Session缓存，再返回角色数据
            List<Role> activeRoles = roleRepository.findActiveRolesByUserId(userId);
            // 提取标识符字符串列表
            roles = new ArrayList<>();
            for (Role item : activeRoles) {
                roles.add(item.getIdentifier());
            }
            // 更新本用户缓存的角色数据
            session.set("roleList", roles);
            setRoleListCacheTime(userId, DateUtil.now());
        }

        return roles;
    }

    /**
     * 公共标记：roleDbRefreshTime           数据库的角色数据更新的时间
     * 公共标记：permissionDbRefreshTime     数据库的权限数据更新的时间
     * 用户标记：permissionListCacheTime     本用户权限数据缓存更新时间
     * <p>
     * 一、由于角色、权限的数据库变更造成的需要大范围用户刷新权限缓存的场景
     * 1、角色的identifier、active变更时，更新roleDbRefreshTime；其他字段变更不打紧。
     * 权限的identifier、active变更时或角色与权限映射关系变更时，更新permissionDbRefreshTime；其他字段变更不打紧。
     * 2、若permissionListCacheTime >= roleDbRefreshTime
     * 且permissionListCacheTime >= permissionDbRefreshTime，
     * 则先查询缓存的权限数据作为结果，查不到缓存再查数据库更新缓存。
     * 3、若不满足上述条件则查数据库更新缓存，刷新缓存后更新permissionListCacheTime。
     * <p>
     * 二、由于用户自身变更造成的自己需要刷新权限缓存的场景
     * 1、用户自身变更时，置用户标记permissionListCacheTime为0。
     * 2、若permissionListCacheTime >= roleDbRefreshTime
     * 且permissionListCacheTime >= permissionDbRefreshTime，
     * 则先查询缓存的权限数据作为结果，查不到缓存再查数据库更新缓存。
     * 3、若不满足上述条件则查数据库更新缓存，刷新缓存后更新permissionListCacheTime。
     */
    @Override
    public List<String> getPermissionList(Long userId) {
        log.info("userId=[{}]", userId);
        List<String> permissions = null;
        SaSession session = StpUtil.getSessionByLoginId(userId);

        Date t1 = (Date) redis.opsForValue().get("roleDbRefreshTime");
        Date t2 = (Date) redis.opsForValue().get("permissionDbRefreshTime");
        Date t3 = (Date) session.get("permissionListCacheTime");
        // 先查询Session缓存的权限数据
        if ((t1 == null || (t3 != null && t3.compareTo(t1) >= 0)) && (t2 == null || (t3 != null && t3.compareTo(t2) >= 0))) {
            // 第一二点第2小点：返回Session缓存的角色数据
            permissions = (List<String>) session.get("permissionList");
        }

        if (permissions == null) {
            // 第一二点第3小点：先用数据库数据刷新Session缓存，再返回角色数据
            List<Permission> activePermissions = permissionRepository.findActivePermissionsByUserId(userId);
            // 提取标识符字符串列表
            permissions = new ArrayList<>();
            for (Permission item : activePermissions) {
                permissions.add(item.getIdentifier());
            }
            // 更新本用户缓存的权限数据
            session.set("permissionList", permissions);
            setPermissionListCacheTime(userId, DateUtil.now());
        }

        return permissions;
    }


    @Override
    public void setRoleListCacheTime(Long userId, Date timestamp) {
        StpUtil.getSessionByLoginId(userId).set("roleListCacheTime", timestamp);
    }

    @Override
    public void setPermissionListCacheTime(Long userId, Date timestamp) {
        StpUtil.getSessionByLoginId(userId).set("permissionListCacheTime", timestamp);
    }
}
