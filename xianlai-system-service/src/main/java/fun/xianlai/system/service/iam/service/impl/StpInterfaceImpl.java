package fun.xianlai.system.service.iam.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SaToken框架获取角色列表和权限列表的StpInterface接口的实现
 *
 * @author WyattLau
 */
@Service
public class StpInterfaceImpl implements StpInterface {
//    @Autowired
//    private FeignUserService userService;
//
//    /**
//     * @param loginId   账号id
//     * @param loginType 账号类型
//     * @return 该账号id具有的权限码集合
//     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return null;
//        return userService.getPermissionList(Long.valueOf(String.valueOf(loginId)));
    }
//
//    /**
//     * @param loginId   账号id
//     * @param loginType 账号类型
//     * @return 该账号id具有的角色标识集合
//     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return null;
//        return userService.getRoleList(Long.valueOf(String.valueOf(loginId)));
    }
}
