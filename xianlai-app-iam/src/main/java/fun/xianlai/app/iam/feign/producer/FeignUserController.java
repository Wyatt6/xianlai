package fun.xianlai.app.iam.feign.producer;

import fun.xianlai.app.iam.service.UserService;
import fun.xianlai.core.response.RetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feign/user")
public class FeignUserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getRoleList")
    public RetResult getRoleList(@RequestParam Long userId) {
        return new RetResult().writeFeignData(userService.getRoleList(userId));
    }

    @GetMapping("/getPermissionList")
    public RetResult getPermissionList(@RequestParam Long userId) {
        return new RetResult().writeFeignData(userService.getPermissionList(userId));
    }
}
