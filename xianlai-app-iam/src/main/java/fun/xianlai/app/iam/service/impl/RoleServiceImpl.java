package fun.xianlai.app.iam.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import fun.xianlai.app.iam.model.entity.rbac.Role;
import fun.xianlai.app.iam.repository.RolePermissionRepository;
import fun.xianlai.app.iam.repository.RoleRepository;
import fun.xianlai.app.iam.repository.UserRoleRepository;
import fun.xianlai.app.iam.service.RoleService;
import fun.xianlai.basic.annotation.ServiceLog;
import fun.xianlai.basic.annotation.SimpleServiceLog;
import fun.xianlai.basic.exception.SysException;
import fun.xianlai.basic.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    @ServiceLog("创建角色")
    public Role createRole(Role role) {
        try {
            log.info("插入记录");
            role.setId(null);
            role = roleRepository.save(role);
            log.info("新角色成功保存到数据库: id=[{}]", role.getId());
            return role;
        } catch (DataIntegrityViolationException e) {
            log.info(e.getMessage());
            throw new SysException("角色标识符重复");
        }
    }

    @Override
    @ServiceLog("删除角色")
    public void deleteRole(Long roleId) {
        log.info("删除与本角色相关的用户-角色关系");
        userRoleRepository.deleteByRoleId(roleId);
        log.info("删除与本角色相关的角色-权限关系");
        rolePermissionRepository.deleteByRoleId(roleId);
        log.info("数据库删除本角色数据");
        roleRepository.deleteById(roleId);
        log.info("更新标记rolesDbRefreshed（数据库的角色数据更新的时间），表示此时间后应当刷新缓存的角色数据");
        setRolesDbRefreshed(new Date());
    }

    @Override
    @ServiceLog("更新角色")
    public Role updateRole(Role role) {
        String identifier = role.getIdentifier();
        String name = role.getName();
        Boolean active = role.getActive();
        Long sortId = role.getSortId();
        String description = role.getDescription();

        log.info("查询是否存在该角色");
        Optional<Role> oldRole = roleRepository.findById(role.getId());
        if (oldRole.isPresent()) {
            log.info("角色存在，组装用来更新的对象");
            Role newRole = oldRole.get();
            if (identifier != null) newRole.setIdentifier(identifier);
            if (name != null) newRole.setName(name);
            if (active != null) newRole.setActive(active);
            if (sortId != null) newRole.setSortId(sortId);
            if (description != null) newRole.setDescription(description);

            try {
                log.info("更新数据库");
                newRole = roleRepository.save(newRole);
            } catch (DataIntegrityViolationException e) {
                log.info(e.getMessage());
                throw new SysException("角色标识重复");
            }

            boolean critical = false;
            if (identifier != null) critical = true;
            if (active != null) critical = true;
            if (critical) {
                log.info("编辑此角色数据影响到用户权限控制，需要更新缓存");
                log.info("更新标记rolesDbRefreshed（数据库的角色数据更新的时间）");
                setRolesDbRefreshed(new Date());
            }
            return newRole;
        } else {
            throw new SysException("要修改的角色不存在");
        }
    }

    @Override
    @SimpleServiceLog("条件查询角色分页")
    public Page<Role> getRolesByPageConditionally(int pageNum, int pageSize, String identifier, String name, Boolean active, String permission) {
        Sort sort = Sort.by(Sort.Order.asc("sortId"));
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return roleRepository.findConditionally(identifier, name, active, permission, pageable);
    }

    @Override
    @SimpleServiceLog("查询某角色的排名")
    public Long getRowNum(Long roleId) {
        return roleRepository.findRowNumById(roleId);
    }

    /**
     * 公共标记：rolesDbRefreshed          数据库的角色数据更新的时间戳
     * 用户标记：rolesCacheOfUserRefreshed  本用户角色数据缓存更新时间戳
     * <p>
     * 一、由于角色的数据库变更造成的需要大范围用户刷新角色缓存的场景
     * 1、角色记录的identifier、active变更时，更新rolesDbRefreshed；其他字段变更不打紧。
     * 2、若rolesCacheOfUserRefreshed >= rolesDbRefreshed表明缓存中的角色数据已经是最新的，先查询缓存的角色数据作为结果，查不到缓存再查数据库更新缓存。
     * 3、若不满足上述条件则查数据库更新缓存。
     * 4、刷新缓存后更新rolesCacheOfUserRefreshed。
     * <p>
     * 二、由于用户自身变更造成的自己需要刷新角色缓存的场景
     * 1、用户自身变更时，置用户标记rolesCacheOfUserRefreshed为0。
     * 2、若rolesCacheOfUserRefreshed >= rolesDbRefreshed表明缓存中的角色数据已经是最新的，则先查询缓存的角色数据作为结果，查不到缓存再查数据库更新缓存。
     * 3、若不满足上述条件则查数据库更新缓存。
     * 4、刷新缓存后更新rolesCacheOfUserRefreshed。
     */
    @Override
    @ServiceLog("从缓存或数据库获取用户生效中的角色标识符")
    public List<String> getActiveRoleIdentifiers(Long userId) {
        log.info("userId=[{}]", userId);
        List<String> roles = null;
        SaSession session = StpUtil.getSessionByLoginId(userId);

        Date t1 = (Date) redis.opsForValue().get("rolesDbRefreshed");
        Date t2 = (Date) session.get("rolesCacheOfUserRefreshed");
        log.info("数据库的角色数据更新的时间戳 rolesDbRefreshed=[{}]", t1 == null ? null : DateUtil.commonFormat(t1));
        log.info("本用户角色数据缓存更新时间戳 rolesCacheOfUserRefreshed=[{}]", t2 == null ? null : DateUtil.commonFormat(t2));
        if (t1 == null || (t2 != null && t2.compareTo(t1) >= 0)) {
            log.info("先查询Session缓存的角色数据");
            roles = (List<String>) session.get("roles");
        }

        if (roles == null) {
            log.info("无缓存或缓存不是最新，查询数据库，并更新缓存");
            List<Role> activeRoles = roleRepository.findActiveRolesByUserId(userId);
            log.info("提取标识符字符串列表");
            roles = new ArrayList<>();
            for (Role item : activeRoles) {
                roles.add(item.getIdentifier());
            }
            log.info("更新缓存的角色数据");
            session.set("roles", roles);
            setRolesCacheOfUserRefreshed(userId, new Date());
        }

        log.info("用户生效中的角色标识列表: {}", roles);
        return roles;
    }

    @Override
    @SimpleServiceLog("设置rolesDbRefreshed时间戳")
    public void setRolesDbRefreshed(Date timestamp) {
        redis.opsForValue().set("rolesDbRefreshed", timestamp);
        log.info("已更新 rolesDbRefreshed 时间戳为: {}", DateUtil.commonFormat(timestamp));
    }

    @Override
    @SimpleServiceLog("设置rolesCacheOfUserRefreshed时间戳")
    public void setRolesCacheOfUserRefreshed(Long userId, Date timestamp) {
        StpUtil.getSessionByLoginId(userId).set("rolesCacheOfUserRefreshed", timestamp);
        log.info("已更新 rolesCacheOfUserRefreshed 时间戳为: {}", DateUtil.commonFormat(timestamp));
    }
}
