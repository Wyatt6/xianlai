package fun.xianlai.core.feign.notlog.consumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author WyattLau
 */
@FeignClient(contextId = "xianlai-app-iam-user-notlog", name = "xianlai-app-iam", path = "/feign/user")
public interface FeignUserService {
    @GetMapping("/getRoleList")
    List<String> getRoleList(@RequestParam Long userId);

    @GetMapping("/getPermissionList")
    List<String> getPermissionList(@RequestParam Long userId);
}
