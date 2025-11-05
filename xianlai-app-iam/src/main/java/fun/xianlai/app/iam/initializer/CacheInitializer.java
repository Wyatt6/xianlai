package fun.xianlai.app.iam.initializer;

import fun.xianlai.app.iam.service.PermissionService;
import fun.xianlai.app.iam.service.RoleService;
import fun.xianlai.core.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author WyattLau
 */
@Slf4j
@Component
public class CacheInitializer implements CommandLineRunner {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化IAM模块缓存");
        permissionService.setPermissionDbRefreshTime(DateUtil.zero());
        roleService.setRoleDbRefreshTime(DateUtil.zero());
        log.info("已完成IAM模块缓存初始化");
    }
}
