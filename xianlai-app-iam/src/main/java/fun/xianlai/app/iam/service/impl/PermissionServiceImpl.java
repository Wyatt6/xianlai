package fun.xianlai.app.iam.service.impl;


import fun.xianlai.app.iam.model.entity.rbac.Permission;
import fun.xianlai.app.iam.model.form.PermissionCondition;
import fun.xianlai.app.iam.repository.PermissionRepository;
import fun.xianlai.app.iam.repository.RolePermissionRepository;
import fun.xianlai.app.iam.service.PermissionService;
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

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Override
    @SimpleServiceLog("创建权限")
    public Permission createPermission(Permission permission) {
        try {
            permission.setId(null);
            return permissionRepository.save(permission);
        } catch (DataIntegrityViolationException e) {
            throw new SysException("权限标识符重复");
        }
    }

    @Override
    @ServiceLog("删除权限")
    public void deletePermission(Long permissionId) {
        log.info("删除与本权限相关的角色-权限关系");
        rolePermissionRepository.deleteByPermissionId(permissionId);
        log.info("数据库删除本权限数据");
        permissionRepository.deleteById(permissionId);
        log.info("更新标记permissionDbRefreshTime（数据库的权限数据更新的时间），表示此时间后应当刷新缓存的权限数据");
        setPermissionDbRefreshTime(DateUtil.now());
    }

    @Override
    @ServiceLog("更新权限")
    public Permission updatePermission(Permission permission) {
        Long sortId = permission.getSortId();
        String identifier = permission.getIdentifier();
        String name = permission.getName();
        String description = permission.getDescription();

        log.info("查询是否存在该权限");
        Optional<Permission> oldPermission = permissionRepository.findById(permission.getId());
        if (oldPermission.isPresent()) {
            log.info("权限存在，组装用来更新的对象");
            Permission newPermission = oldPermission.get();
            if (sortId != null) newPermission.setSortId(sortId);
            if (identifier != null) newPermission.setIdentifier(identifier);
            if (name != null) newPermission.setName(name);
            if (description != null) newPermission.setDescription(description);

            try {
                log.info("更新数据库");
                newPermission = permissionRepository.save(newPermission);
            } catch (DataIntegrityViolationException e) {
                log.info(e.getMessage());
                throw new SysException("权限标识重复");
            }

            if (identifier != null) {
                log.info("编辑此权限数据影响到用户权限控制，需要更新缓存");
                log.info("更新标记permissionDbRefreshTime（数据库的权限数据更新的时间）");
                setPermissionDbRefreshTime(DateUtil.now());
            }
            return newPermission;
        } else {
            throw new SysException("要修改的权限不存在");
        }
    }

    @Override
    @SimpleServiceLog("获取全量权限数据")
    public List<Permission> listAllPermissions() {
        Sort sort = Sort.by(Sort.Order.asc("sortId"), Sort.Order.asc("identifier"));
        return permissionRepository.findAll(sort);
    }

    @Override
    @SimpleServiceLog("获取某角色的权限ID列表")
    public List<Long> getPermissionIdsOfRole(Long roleId) {
        return permissionRepository.findIdsByRoleId(roleId);
    }

    @Override
    @ServiceLog("条件查询权限分页")
    public Page<Permission> getPermissionsByPageConditionally(int pageNum, int pageSize, PermissionCondition condition) {
        String identifier = (condition == null || condition.getIdentifier() == null) ? null : condition.getIdentifier();
        String name = (condition == null || condition.getName() == null) ? null : condition.getName();
        String description = (condition == null || condition.getDescription() == null) ? null : condition.getDescription();

        Sort sort = Sort.by(Sort.Order.asc("sortId"), Sort.Order.asc("identifier"));
        if (pageNum >= 0 && pageSize > 0) {
            log.info("分页查询");
            Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
            return permissionRepository.findConditionally(identifier, name, description, pageable);
        } else {
            log.info("全表查询");
            return permissionRepository.findConditionally(identifier, name, description, Pageable.unpaged(sort));
        }
    }

    @Override
    public Long getRowNum(Long permissionId) {
        return permissionRepository.findRowNumById(permissionId);
    }

    @Override
    public void setPermissionDbRefreshTime(Date timestamp) {
        redis.opsForValue().set("permissionDbRefreshTime", timestamp);
    }
}
