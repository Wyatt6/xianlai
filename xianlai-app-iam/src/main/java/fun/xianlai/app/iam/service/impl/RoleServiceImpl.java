package fun.xianlai.app.iam.service.impl;

import fun.xianlai.app.iam.model.entity.rbac.Role;
import fun.xianlai.app.iam.model.entity.rbac.RolePermission;
import fun.xianlai.app.iam.model.form.RoleCondition;
import fun.xianlai.app.iam.repository.RolePermissionRepository;
import fun.xianlai.app.iam.repository.RoleRepository;
import fun.xianlai.app.iam.repository.UserRoleRepository;
import fun.xianlai.app.iam.service.PermissionService;
import fun.xianlai.app.iam.service.RoleService;
import fun.xianlai.core.annotation.ServiceLog;
import fun.xianlai.core.annotation.SimpleServiceLog;
import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.utils.DateUtil;
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
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;
    @Autowired
    private PermissionService permissionService;

    @Override
    @SimpleServiceLog("创建角色")
    public Role createRole(Role role) {
        try {
            role.setId(null);
            return roleRepository.save(role);
        } catch (DataIntegrityViolationException e) {
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
        log.info("更新标记roleDbRefreshTime（数据库的角色数据更新的时间），表示此时间后应当刷新缓存的角色数据");
        setRoleDbRefreshTime(DateUtil.now());
    }

    @Override
    @ServiceLog("更新角色")
    public Role updateRole(Role role) {
        Long sortId = role.getSortId();
        String identifier = role.getIdentifier();
        String name = role.getName();
        String description = role.getDescription();
        Boolean active = role.getActive();
        Boolean bindCheck = role.getBindCheck();

        log.info("查询是否存在该角色");
        Optional<Role> oldRole = roleRepository.findById(role.getId());
        if (oldRole.isPresent()) {
            log.info("角色存在，组装用来更新的对象");
            Role newRole = oldRole.get();
            if (sortId != null) newRole.setSortId(sortId);
            if (identifier != null) newRole.setIdentifier(identifier);
            if (name != null) newRole.setName(name);
            if (description != null) newRole.setDescription(description);
            if (active != null) newRole.setActive(active);
            if (bindCheck != null) newRole.setBindCheck(bindCheck);

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
                log.info("更新标记roleDbRefreshTime（数据库的角色数据更新的时间）");
                setRoleDbRefreshTime(DateUtil.now());
            }
            return newRole;
        } else {
            throw new SysException("要修改的角色不存在");
        }
    }

    @Override
    @ServiceLog("条件查询角色分页")
    public Page<Role> getRolesByPageConditionally(int pageNum, int pageSize, RoleCondition condition) {
        String identifier = (condition == null || condition.getIdentifier() == null) ? null : condition.getIdentifier();
        String name = (condition == null || condition.getName() == null) ? null : condition.getName();
        String description = (condition == null || condition.getDescription() == null) ? null : condition.getDescription();
        Boolean active = (condition == null || condition.getActive() == null) ? null : condition.getActive();
        Boolean bindCheck = (condition == null || condition.getBindCheck() == null) ? null : condition.getBindCheck();
        String permission = (condition == null || condition.getPermission() == null) ? null : condition.getPermission();

        Sort sort = Sort.by(Sort.Order.asc("sortId"), Sort.Order.asc("identifier"));
        if (pageNum >= 0 && pageSize > 0) {
            log.info("分页查询");
            Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
            return roleRepository.findConditionally(identifier, name, description, active, bindCheck, permission, pageable);
        } else {
            log.info("全表查询");
            return roleRepository.findConditionally(identifier, name, description, active, bindCheck, permission, Pageable.unpaged(sort));
        }
    }

    @Override
    @SimpleServiceLog("查询某角色的排名")
    public Long getRowNum(Long roleId) {
        return roleRepository.findRowNumById(roleId);
    }

    @Override
    @SimpleServiceLog("获取某用户的角色ID列表")
    public List<Long> getRoleIdsOfUser(Long userId) {
        return roleRepository.findIdsByUserId(userId);
    }

    @Override
    public void setRoleDbRefreshTime(Date timestamp) {
        redis.opsForValue().set("roleDbRefreshTime", timestamp);
    }

    @Override
    @ServiceLog("授权")
    public List<Long> grant(Long roleId, List<Long> permissionIds) {
        List<Long> failList = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            try {
                RolePermission rp = new RolePermission();
                rp.setRoleId(roleId);
                rp.setPermissionId(permissionId);
                rolePermissionRepository.save(rp);
                log.info("授权成功: (roleId=[{}], permissionId=[{}])", roleId, permissionId);
            } catch (Exception e) {
                failList.add(permissionId);
            }
        }
        if (failList.size() < permissionIds.size()) {
            log.info("有授权成功，要更新permissionDbRefreshTime时间戳，以动态更新用户权限缓存");
            permissionService.setPermissionDbRefreshTime(DateUtil.now());
        }
        return failList;
    }

    @Override
    @ServiceLog("解除授权")
    public List<Long> cancelGrant(Long roleId, List<Long> permissionIds) {
        List<Long> failList = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            try {
                RolePermission rp = new RolePermission();
                rp.setRoleId(roleId);
                rp.setPermissionId(permissionId);
                rolePermissionRepository.delete(rp);
                log.info("解除授权成功: (roleId=[{}], permissionId=[{}])", roleId, permissionId);
            } catch (Exception e) {
                failList.add(permissionId);
            }
        }
        if (failList.size() < permissionIds.size()) {
            log.info("有解除授权成功，要更新permissionDbRefreshTime时间戳，以动态更新用户权限缓存");
            permissionService.setPermissionDbRefreshTime(DateUtil.now());
        }
        return failList;
    }
}
