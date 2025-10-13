package fun.xianlai.app.iam.service.impl;


import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import fun.xianlai.app.iam.model.entity.rbac.Permission;
import fun.xianlai.app.iam.repository.PermissionRepository;
import fun.xianlai.app.iam.repository.RolePermissionRepository;
import fun.xianlai.app.iam.service.PermissionService;
import fun.xianlai.basic.annotation.ServiceLog;
import fun.xianlai.basic.annotation.SimpleServiceLog;
import fun.xianlai.basic.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

//    @Override
//    public Permission createPermission(Permission permission) {
//        log.info("[[创建权限对象]]");
//        Assert.notNull(permission, "新权限数据为空");
//        Assert.hasText(permission.getIdentifier(), "新权限标识符为空");
//        log.info("输入参数: {}", permission);
//        try {
//            log.info("插入记录");
//            permission.setId(null);
//            if (permission.getactive() == null) permission.setactive(false);
//            permission.setCreateTime(new Date());
//            permission = permissionRepository.save(permission);
//            log.info("新权限成功保存到数据库: id=[{}]", permission.getId());
//            return permission;
//        } catch (DataIntegrityViolationException e) {
//            log.info(e.getMessage());
//            throw new SystemException("权限标识符重复");
//        }
//    }

    @Override
    @ServiceLog("删除权限")
    public void deletePermission(Long permissionId) {
        log.info("删除与本权限相关的角色-权限关系");
        rolePermissionRepository.deleteByPermissionId(permissionId);
        log.info("数据库删除本权限数据");
        permissionRepository.deleteById(permissionId);
        log.info("更新标记permissionsDbRefreshed（数据库的权限数据更新的时间），表示此时间后应当刷新缓存的权限数据");
        setPermissionsDbRefreshed(new Date());
    }

//    @Override
//    public Permission updatePermission(Permission permission) {
//        log.info("[[更新权限对象]]");
//        Assert.notNull(permission, "权限数据为空");
//        Assert.notNull(permission.getId(), "权限ID为空");
//        log.info("输入参数: {}", permission);
//
//        String module = permission.getModule();
//        String identifier = permission.getIdentifier();
//        String name = permission.getName();
//        Boolean active = permission.getactive();
//        Date createTime = permission.getCreateTime();
//        String remark = permission.getRemark();
//
//        log.info("查询是否存在该权限");
//        Optional<Permission> oldPermission = permissionRepository.findById(permission.getId());
//        if (oldPermission.isPresent()) {
//            log.info("权限存在，组装用来更新的对象");
//            Permission newPermission = oldPermission.get();
//            if (module != null) newPermission.setModule(module);
//            if (identifier != null) newPermission.setIdentifier(identifier);
//            if (name != null) newPermission.setName(name);
//            if (active != null) newPermission.setactive(active);
//            if (createTime != null) newPermission.setCreateTime(createTime);
//            if (remark != null) newPermission.setRemark(remark);
//
//            try {
//                log.info("更新数据库");
//                newPermission = permissionRepository.save(newPermission);
//            } catch (DataIntegrityViolationException e) {
//                log.info(e.getMessage());
//                throw new SystemException("权限标识重复");
//            }
//
//            boolean critical = false;
//            if (identifier != null) critical = true;
//            if (active != null) critical = true;
//            if (critical) {
//                log.info("编辑此权限数据影响到用户权限控制，需要更新缓存");
//                log.info("更新标记permissionsDbRefreshed（数据库的权限数据更新的时间）");
//                setPermissionsDbRefreshed(new Date());
//            }
//            return newPermission;
//        } else {
//            throw new SystemException("要修改的权限不存在");
//        }
//    }

//    @Override
//    public List<Permission> listPermissions() {
//        log.info("[[获取全量权限数据]]");
//        Sort sort = Sort.by(
//                Sort.Order.asc("module"),
//                Sort.Order.asc("identifier")
//        );
//        return permissionRepository.findAll(sort);
//    }
//
//    @Override
//    public List<Long> getPermissionIdsOfRole(Long roleId) {
//        log.info("[[获取某角色的权限]]");
//        Assert.notNull(roleId, "角色ID为空");
//        log.info("输入参数: roleId=[{}]", roleId);
//
//        return permissionRepository.findIdsByRoleId(roleId);
//    }

    @Override
    @SimpleServiceLog("条件查询权限分页")
    public Page<Permission> getPermissionsByPageConditionally(int pageNum, int pageSize, Long id, String identifier, String name) {
        Sort sort = Sort.by(Sort.Order.asc("sortId"), Sort.Order.asc("identifier"));
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return permissionRepository.findConditionally(id, identifier, name, pageable);
    }

//    @Override
//    public Long getRowNum(Long permissionId) {
//        return permissionRepository.findRowNumById(permissionId);
//    }

    /**
     * 公共标记：rolesDbRefreshed                  数据库的角色数据更新的时间
     * 公共标记：permissionsDbRefreshed            数据库的权限数据更新的时间
     * 用户标记：permissionsCacheOfUserRefreshed    本用户权限数据缓存更新时间
     * <p>
     * 一、由于角色、权限的数据库变更造成的需要大范围用户刷新权限缓存的场景
     * 1、角色的identifier、active变更时，更新rolesDbRefreshed；其他字段变更不打紧。
     * 权限的identifier、active变更时或角色与权限映射关系变更时，更新permissionsDbRefreshed；其他字段变更不打紧。
     * 2、若permissionsCacheOfUserRefreshed >= rolesDbRefreshed
     * 且permissionsCacheOfUserRefreshed >= permissionsDbRefreshed，
     * 则先查询缓存的权限数据作为结果，查不到缓存再查数据库更新缓存。
     * 3、若不满足上述条件则查数据库更新缓存。
     * 4、刷新缓存后更新permissionsCacheOfUserRefreshed。
     * <p>
     * 二、由于用户自身变更造成的自己需要刷新权限缓存的场景
     * 1、用户自身变更时，置用户标记permissionsCacheOfUserRefreshed为0。
     * 2、若permissionsCacheOfUserRefreshed >= rolesDbRefreshed
     * 且permissionsCacheOfUserRefreshed >= permissionsDbRefreshed，
     * 则先查询缓存的权限数据作为结果，查不到缓存再查数据库更新缓存。
     * 3、若不满足上述条件则查数据库更新缓存。
     * 4、刷新缓存后更新permissionsCacheOfUserRefreshed。
     */
    @Override
    @ServiceLog("从缓存或数据库获取用户生效中的权限标识符")
    public List<String> getActivePermissionIdentifiers(Long userId) {
        log.info("userId=[{}]", userId);
        List<String> permissions = null;
        SaSession session = StpUtil.getSessionByLoginId(userId);

        Date t1 = (Date) redis.opsForValue().get("rolesDbRefreshed");
        Date t2 = (Date) redis.opsForValue().get("permissionsDbRefreshed");
        Date t3 = (Date) session.get("permissionsCacheOfUserRefreshed");
        log.info("本用户角色数据缓存更新时间 rolesDbRefreshed=[{}]", t1 == null ? null : DateUtil.commonFormat(t1));
        log.info("本用户权限数据缓存更新时间 permissionsDbRefreshed=[{}]", t2 == null ? null : DateUtil.commonFormat(t2));
        log.info("数据库的权限数据更新的时间 permissionsCacheOfUserRefreshed:=[{}]", t3 == null ? null : DateUtil.commonFormat(t3));

        if ((t1 == null || (t3 != null && t3.compareTo(t1) >= 0)) && (t2 == null || (t3 != null && t3.compareTo(t2) >= 0))) {
            log.info("先查询Session缓存的权限数据");
            permissions = (List<String>) session.get("permissions");
        }

        if (permissions == null) {
            log.info("无缓存或缓存不是最新，查询数据库，并更新缓存");
            List<Permission> activePermissions = permissionRepository.findActivePermissionsByUserId(userId);
            log.info("提取标识符字符串列表");
            permissions = new ArrayList<>();
            for (Permission item : activePermissions) {
                permissions.add(item.getIdentifier());
            }
            log.info("更新缓存的权限数据");
            session.set("permissions", permissions);
            setPermissionsCacheOfUserRefreshed(userId, new Date());
        }

        log.info("用户生效中的权限标识列表: {}", permissions);
        return permissions;
    }

    @Override
    @SimpleServiceLog("设置permissionsDbRefreshed时间戳")
    public void setPermissionsDbRefreshed(Date timestamp) {
        redis.opsForValue().set("permissionsDbRefreshed", timestamp);
        log.info("已更新permissionsDbRefreshed时间戳为: {}", DateUtil.commonFormat(timestamp));
    }

    @Override
    @SimpleServiceLog("设置permissionsCacheOfUserRefreshed时间戳")
    public void setPermissionsCacheOfUserRefreshed(Long userId, Date timestamp) {
        StpUtil.getSessionByLoginId(userId).set("permissionsCacheOfUserRefreshed", timestamp);
        log.info("已更新permissionsCacheOfUserRefreshed时间戳为: {}", DateUtil.commonFormat(timestamp));
    }
}
