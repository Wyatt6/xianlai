package fun.xianlai.app.iam.service.impl;

import fun.xianlai.app.iam.model.entity.rbac.Role;
import fun.xianlai.app.iam.model.entity.rbac.RolePermission;
import fun.xianlai.app.iam.repository.RolePermissionRepository;
import fun.xianlai.app.iam.repository.RoleRepository;
import fun.xianlai.app.iam.repository.UserRoleRepository;
import fun.xianlai.app.iam.service.PermissionService;
import fun.xianlai.app.iam.service.RoleService;
import fun.xianlai.core.annotation.ServiceLog;
import fun.xianlai.core.annotation.SimpleServiceLog;
import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.response.DataMap;
import fun.xianlai.core.utils.bean.BeanUtils;
import fun.xianlai.core.utils.time.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @SimpleServiceLog("创建角色")
    @Transactional
    public DataMap add(Role role) {
        try {
            role.setId(null);
            Role savedRole = roleRepository.save(role);
            Long rowNum = roleRepository.findRowNumById(savedRole.getId());
            DataMap result = new DataMap();
            result.put("role", savedRole);
            result.put("rowNum", rowNum);
            return result;
        } catch (DataIntegrityViolationException e) {
            throw new SysException("角色标识符重复");
        }
    }

    @Override
    @ServiceLog("删除角色")
    @Transactional
    public void delete(Long roleId) {
        log.info("删除与本角色相关的用户-角色关系");
        userRoleRepository.deleteByRoleId(roleId);
        log.info("删除与本角色相关的角色-权限关系");
        rolePermissionRepository.deleteByRoleId(roleId);
        log.info("数据库删除本角色数据");
        roleRepository.deleteById(roleId);
        log.info("更新标记roleDbRefreshTime（数据库的角色数据更新的时间），表示此时间后应当刷新缓存的角色数据");
        setRoleDbRefreshTime(DateUtils.now());
    }

    @Override
    @ServiceLog("更新角色")
    @Transactional
    public DataMap edit(Role role) {
        Optional<Role> oldRole = roleRepository.findById(role.getId());
        if (oldRole.isPresent()) {
            Role newRole = oldRole.get();
            BeanUtils.copyPropertiesNotNull(role, newRole);

            try {
                log.info("更新数据库");
                newRole = roleRepository.save(newRole);
            } catch (DataIntegrityViolationException e) {
                log.info(e.getMessage());
                throw new SysException("角色标识重复");
            }

            boolean critical = false;
            if (role.getIdentifier() != null) critical = true;
            if (role.getActive() != null) critical = true;
            if (critical) {
                log.info("编辑此角色数据影响到用户权限控制，需要更新缓存");
                log.info("更新标记roleDbRefreshTime（数据库的角色数据更新的时间）");
                setRoleDbRefreshTime(DateUtils.now());
            }
            return new DataMap("role", newRole);
        } else {
            throw new SysException("要修改的角色不存在");
        }
    }

    @Override
    @ServiceLog("条件查询角色分页")
    public Page<Role> getPageConditionally(int pageNum, int pageSize, Role condition) {
        String identifier = BeanUtils.getFieldValue(condition, "identifier", String.class);
        String name = BeanUtils.getFieldValue(condition, "name", String.class);
        String description = BeanUtils.getFieldValue(condition, "description", String.class);
        Boolean active = BeanUtils.getFieldValue(condition, "active", Boolean.class);
        Boolean bindCheck = BeanUtils.getFieldValue(condition, "bindCheck", Boolean.class);
        String permission = BeanUtils.getFieldValue(condition, "permission", String.class);

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
    @SimpleServiceLog("获取某用户的角色ID列表")
    public List<Long> getRoleIdsOfUser(Long userId) {
        return roleRepository.findIdsByUserId(userId);
    }

    @Override
    public void setRoleDbRefreshTime(Date timestamp) {
        redis.opsForValue().set("roleDbRefreshTime", timestamp);
    }
}
