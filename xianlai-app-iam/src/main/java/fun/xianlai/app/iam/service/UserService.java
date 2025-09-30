package fun.xianlai.app.iam.service;

/**
 * @author WyattLau
 */
public interface UserService {
    boolean checkUsernameFormat(String username);

    boolean checkPasswordFormat(String password);

    /**
     * 创建新用户
     *
     * @param username 用户名
     * @param password 密码
     */
    void createUser(String username, String password);
}
