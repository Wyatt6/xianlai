package fun.xianlai.app.iam.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import fun.xianlai.app.iam.model.entity.rbac.Permission;
import fun.xianlai.app.iam.model.entity.rbac.Role;
import fun.xianlai.app.iam.model.entity.rbac.User;
import fun.xianlai.app.iam.model.entity.rbac.UserRole;
import fun.xianlai.app.iam.model.form.UserCondition;
import fun.xianlai.app.iam.model.form.UserForm;
import fun.xianlai.app.iam.repository.PermissionRepository;
import fun.xianlai.app.iam.repository.RoleRepository;
import fun.xianlai.app.iam.repository.UserRepository;
import fun.xianlai.app.iam.repository.UserRoleRepository;
import fun.xianlai.app.iam.service.PermissionService;
import fun.xianlai.app.iam.service.RoleService;
import fun.xianlai.app.iam.service.UserService;
import fun.xianlai.core.annotation.ServiceLog;
import fun.xianlai.core.annotation.SimpleServiceLog;
import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.feign.consumer.FeignOptionService;
import fun.xianlai.core.utils.DateUtil;
import fun.xianlai.core.utils.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
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
    @SimpleServiceLog("身份验证（用户名+密码）")
    public User authentication(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(PasswordUtil.encode(password, user.getSalt()))) {
            throw new SysException("用户名或密码错误");
        }
        return user;
    }

    @Override
    @SimpleServiceLog("身份验证（用户ID+密码）")
    public User authentication(Long userId, String password) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty() || !user.get().getPassword().equals(PasswordUtil.encode(password, user.get().getSalt()))) {
            throw new SysException("用户名或密码错误");
        }
        return user.get();
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

    @Override
    @SimpleServiceLog("修改密码")
    public void changePassword(Long userId, String password) {
        String salt = PasswordUtil.generateSalt();
        String encryptedPassword = PasswordUtil.encode(password, salt);
        Optional<User> oldUser = userRepository.findById(userId);
        if (oldUser.isPresent()) {
            User newUser = oldUser.get();
            newUser.setPassword(encryptedPassword);
            newUser.setSalt(salt);
            userRepository.save(newUser);
        } else {
            throw new SysException("用户不存在");
        }
    }

    @Override
    @ServiceLog("条件查询用户分页")
    public Page<User> getUsersByPageConditionally(int pageNum, int pageSize, UserCondition condition) {
        String username = (condition == null || condition.getUsername() == null) ? null : condition.getUsername();
        Date stRegisterTime = (condition == null || condition.getStRegisterTime() == null) ? null : condition.getStRegisterTime();
        Date edRegisterTime = (condition == null || condition.getEdRegisterTime() == null) ? null : condition.getEdRegisterTime();
        Boolean active = (condition == null || condition.getActive() == null) ? null : condition.getActive();
        String role = (condition == null || condition.getRole() == null) ? null : condition.getRole();
        String permission = (condition == null || condition.getPermission() == null) ? null : condition.getPermission();

        Sort sort = Sort.by(Sort.Order.asc("registerTime"));
        if (pageNum >= 0 && pageSize > 0) {
            log.info("分页查询");
            Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
            return userRepository.findConditionally(
                    username,
                    stRegisterTime,
                    edRegisterTime,
                    active,
                    role,
                    permission,
                    pageable);
        } else {
            log.info("全表查询");
            return userRepository.findConditionally(
                    username,
                    stRegisterTime,
                    edRegisterTime,
                    active,
                    role,
                    permission,
                    Pageable.unpaged(sort));
        }
    }

    @Override
    @ServiceLog("绑定")
    public List<Long> bind(Long userId, List<Long> roleIds) {
        List<Role> bindCheckList = roleRepository.findByBindCheck(true);
        List<String> roleList = StpUtil.getRoleList(userId);
        List<Long> failList = new ArrayList<>();
        for (Long roleId : roleIds) {
            try {
                for (Role item : bindCheckList) {
                    if (roleId.equals(item.getId())) {
                        if (!roleList.contains(MessageFormat.format("user:bind:{0}", item.getIdentifier()))) {
                            throw new SysException(MessageFormat.format("权限不足，无法为用户绑定角色[{0} {1}]", item.getId(), item.getIdentifier()));
                        }
                        break;
                    }
                }
                UserRole ur = new UserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                userRoleRepository.save(ur);
                log.info("绑定成功: (userId=[{}], roleId=[{}])", userId, roleId);
            } catch (Exception e) {
                failList.add(roleId);
            }
        }
        if (failList.size() < roleIds.size()) {
            log.info("有绑定成功，要更新roleListCacheTime和permissionListCacheTime时间戳，以动态更新用户权限缓存");
            setRoleListCacheTime(userId, DateUtil.zero());
            setPermissionListCacheTime(userId, DateUtil.zero());
        }
        return failList;
    }

    @Override
    @ServiceLog("解除绑定")
    public List<Long> cancelBind(Long userId, List<Long> roleIds) {
        List<Role> bindCheckList = roleRepository.findByBindCheck(true);
        List<String> roleList = StpUtil.getRoleList(userId);
        List<Long> failList = new ArrayList<>();
        for (Long roleId : roleIds) {
            try {
                for (Role item : bindCheckList) {
                    if (roleId.equals(item.getId())) {
                        if (!roleList.contains(MessageFormat.format("user:bind:{0}", item.getIdentifier()))) {
                            throw new SysException(MessageFormat.format("权限不足，无法为用户解除绑定角色[{0} {1}]", item.getId(), item.getIdentifier()));
                        }
                        break;
                    }
                }
                UserRole ur = new UserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                userRoleRepository.delete(ur);
                log.info("解除绑定成功: (userId=[{}], roleId=[{}])", userId, roleId);
            } catch (Exception e) {
                failList.add(roleId);
            }
        }
        if (failList.size() < roleIds.size()) {
            log.info("有解除绑定成功，要更新roleListCacheTime和permissionListCacheTime时间戳，以动态更新用户权限缓存");
            setRoleListCacheTime(userId, DateUtil.zero());
            setPermissionListCacheTime(userId, DateUtil.zero());
        }
        return failList;
    }

    @Override
    @Transactional
    public UserForm editUserInfo(UserForm form) {
        User user = form.extractToUser();
        Optional<User> oldUser = userRepository.findById(user.getId());
        if (oldUser.isEmpty()) {
            throw new SysException("要修改的用户不存在");
        }
        User newUser = oldUser.get();
        if (user.getUsername() != null) newUser.setUsername(user.getUsername());
        if (user.getActive() != null) newUser.setActive(user.getActive());

        try {
            log.info("更新数据库");
            UserForm result = new UserForm();
            result.importFromUser(userRepository.save(newUser));
            return result;
        } catch (DataIntegrityViolationException e) {
            log.info(e.getMessage());
            throw new SysException("用户名已存在");
        }
    }
}
