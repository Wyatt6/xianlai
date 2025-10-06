package fun.xianlai.app.iam.service.impl;

import fun.xianlai.app.iam.model.entity.rbac.User;
import fun.xianlai.app.iam.repository.UserRepository;
import fun.xianlai.app.iam.service.UserService;
import fun.xianlai.app.iam.feign.consumer.FeignOptionService;
import fun.xianlai.basic.annotation.ServiceLog;
import fun.xianlai.basic.annotation.SimpleServiceLog;
import fun.xianlai.basic.exception.SysException;
import fun.xianlai.basic.utils.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private FeignOptionService optionService;
    @Autowired
    private UserRepository userRepository;

    @Override
    @SimpleServiceLog("检查用户名格式服务")
    public boolean checkUsernameFormat(String username) {
        String USERNAME_REGEXP = optionService.readValueInString("user.username.regexp").orElse("^[a-zA-Z][a-zA-Z_0-9]{4,19}$");
        return username.matches(USERNAME_REGEXP);
    }

    @Override
    @SimpleServiceLog("检查密码格式服务")
    public boolean checkPasswordFormat(String password) {
        String PASSWORD_REGEXP = optionService.readValueInString("user.password.regexp").orElse("^[a-zA-Z_0-9.~!@#$%^&*?]{6,30}$");
        return password.matches(PASSWORD_REGEXP);
    }

    @Override
    @ServiceLog("创建新用户")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public User createUser(String username, String password) {
        log.info("检查用户名是否已被注册");
        if (userRepository.findByUsername(username) != null) {
            throw new SysException("用户名已被使用");
        }

        log.info("密码加密");
        String salt = PasswordUtil.generateSalt();
        String encryptedPassword = PasswordUtil.encode(password, salt);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(encryptedPassword);
        newUser.setSalt(salt);
        newUser.setRegisterTime(new Date());
        newUser.setActive(true);
        User savedUser = userRepository.save(newUser);
        log.info("成功创建新用户: id=[{}]", savedUser.getId());

        return savedUser;
    }

    @Override
    @ServiceLog("身份验证服务")
    public User authentication(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(PasswordUtil.encode(password, user.getSalt()))) {
            throw new SysException("用户名或密码错误");
        }
        log.info("身份验证通过");
        return user;
    }
}
