package fun.xianlai.app.iam.service.impl;


import fun.xianlai.app.iam.model.entity.rbac.Permission;
import fun.xianlai.app.iam.repository.PermissionRepository;
import fun.xianlai.app.iam.repository.RolePermissionRepository;
import fun.xianlai.app.iam.service.PermissionService;
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
    @SimpleServiceLog("新增权限")
    @Transactional
    public DataMap add(Permission permission) {
        try {
            permission.setId(null);
            Permission savedPermission = permissionRepository.save(permission);
            Long rowNum = permissionRepository.findRowNumById(savedPermission.getId());
            DataMap result = new DataMap();
            result.put("permission", savedPermission);
            result.put("rowNum", rowNum);
            return result;
        } catch (DataIntegrityViolationException e) {
            throw new SysException("权限标识符重复");
        }
    }

    @Override
    @ServiceLog("删除权限")
    @Transactional
    public void delete(Long permissionId) {
        log.info("删除与本权限相关的角色-权限关系");
        rolePermissionRepository.deleteByPermissionId(permissionId);
        log.info("数据库删除本权限数据");
        permissionRepository.deleteById(permissionId);
        log.info("更新标记permissionDbRefreshTime（数据库的权限数据更新的时间），表示此时间后应当刷新缓存的权限数据");
        setPermissionDbRefreshTime(DateUtils.now());
    }

    @Override
    @ServiceLog("修改权限")
    @Transactional
    public DataMap edit(Permission permission) {
        Optional<Permission> oldPermission = permissionRepository.findById(permission.getId());
        if (oldPermission.isPresent()) {
            Permission newPermission = oldPermission.get();
            BeanUtils.copyPropertiesNotNull(permission, newPermission);
            try {
                newPermission = permissionRepository.save(newPermission);
            } catch (DataIntegrityViolationException e) {
                log.info(e.getMessage());
                throw new SysException("权限标识重复");
            }
            if (permission.getIdentifier() != null) {
                log.info("编辑此权限数据影响到用户权限控制，需要更新缓存");
                log.info("更新标记permissionDbRefreshTime（数据库的权限数据更新的时间）");
                setPermissionDbRefreshTime(DateUtils.now());
            }
            return new DataMap("permission", newPermission);
        } else {
            throw new SysException("要修改的权限不存在");
        }
    }

    @Override
    @ServiceLog("条件查询权限分页")
    public Page<Permission> getPageConditionally(int pageNum, int pageSize, Permission condition) {
        String identifier = BeanUtils.getFieldValue(condition, "identifier", String.class);
        String name = BeanUtils.getFieldValue(condition, "name", String.class);
        String description = BeanUtils.getFieldValue(condition, "description", String.class);

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
    @SimpleServiceLog("获取某角色的权限ID列表")
    public List<Long> getPermissionIdsOfRole(Long roleId) {
        return permissionRepository.findIdsByRoleId(roleId);
    }

    @Override
    public void setPermissionDbRefreshTime(Date timestamp) {
        redis.opsForValue().set("permissionDbRefreshTime", timestamp);
    }
}
