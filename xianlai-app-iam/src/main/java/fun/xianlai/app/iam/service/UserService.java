package fun.xianlai.app.iam.service;

import fun.xianlai.app.iam.model.entity.other.Profile;
import fun.xianlai.app.iam.model.entity.rbac.User;
import fun.xianlai.app.iam.model.form.UserCondition;
import fun.xianlai.app.iam.model.entity.other.UserInfo;
import fun.xianlai.core.response.DataMap;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 */
public interface UserService {

    boolean matchUsernameFormat(String username);

    boolean matchPasswordFormat(String password);

    /**
     * 身份验证（用户名+密码）
     * 若验证成功则返回用户对象
     *
     * @param username 用户名
     * @param password 密码（明文）
     * @return 用户对象
     */
    User authentication(String username, String password);
}
