package fun.xianlai.system.service.iam.producer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feign/user")
public class FeignUserController {
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("/getRoleList")
//    public RetResult getRoleList(@RequestParam Long userId) {
//        return new RetResult().writeFeignData(userService.getRoleList(userId));
//    }
//
//    @GetMapping("/getPermissionList")
//    public RetResult getPermissionList(@RequestParam Long userId) {
//        return new RetResult().writeFeignData(userService.getPermissionList(userId));
//    }
}
