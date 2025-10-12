package fun.xianlai.app.iam.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import fun.xianlai.app.iam.model.entity.rbac.Role;
import fun.xianlai.app.iam.repository.RoleRepository;
import fun.xianlai.app.iam.service.RoleService;
import fun.xianlai.basic.annotation.ServiceLog;
import fun.xianlai.basic.annotation.SimpleServiceLog;
import fun.xianlai.basic.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private RoleRepository roleRepository;

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
    @SimpleServiceLog("设置rolesCacheOfUserRefreshed时间戳")
    public void setRolesCacheOfUserRefreshed(Long userId, Date timestamp) {
        StpUtil.getSessionByLoginId(userId).set("rolesCacheOfUserRefreshed", timestamp);
        log.info("已更新 rolesCacheOfUserRefreshed 时间戳为: {}", DateUtil.commonFormat(timestamp));
    }
}
