package fun.xianlai.microservice.iam.service;

/**
 * @author WyattLau
 */
public interface UserService {
    boolean checkUsernameFormat(String username);

    boolean checkPasswordFormat(String password);
}
