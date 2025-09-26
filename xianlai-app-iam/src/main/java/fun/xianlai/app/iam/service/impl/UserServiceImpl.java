package fun.xianlai.app.iam.service.impl;

import fun.xianlai.basic.annotation.SimpleServiceLog;
import fun.xianlai.app.iam.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Override
    @SimpleServiceLog("检查用户名格式服务")
    public boolean checkUsernameFormat(String username) {
//        String USERNAME_REGEXP = optionService.readValueForString("user.username.regexp").orElse("^[a-zA-Z][a-zA-Z_0-9]{4,19}$");
//        return username.matches(USERNAME_REGEXP);
        return true;
    }

    @Override
    @SimpleServiceLog("检查密码格式服务")
    public boolean checkPasswordFormat(String password) {
//        String PASSWORD_REGEXP = optionService.readValueForString("user.password.regexp").orElse("^[a-zA-Z_0-9.~!@#$%^&*?]{6,30}$");
//        return password.matches(PASSWORD_REGEXP);
        return true;
    }
}
