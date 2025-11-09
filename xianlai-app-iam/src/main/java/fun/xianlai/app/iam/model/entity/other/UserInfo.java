package fun.xianlai.app.iam.model.entity.other;

import fun.xianlai.app.iam.model.entity.rbac.User;
import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.utils.EntityUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    // User
    private Long id;
    private String username;
    private Date registerTime;
    private Boolean active;
    private Boolean isDelete;
    // Profile
    private Long userId;
    private String avatar;
    private String nickname;
    private String photo;
    private String name;
    private String gender;
    private String employeeNo;
    private String phone;
    private String email;

    public User extractToUser() {
        User user = new User();
        EntityUtil.convert(this, user);
        return user;
    }

    public void importFromUser(User user) {
        EntityUtil.convert(user, this);
        if (this.id != null && this.userId != null && !this.id.equals(this.userId)) {
            throw new SysException("用户信息错误");
        }
    }

    public Profile extractToProfile() {
        Profile profile = new Profile();
        EntityUtil.convert(this, profile);
        return profile;
    }

    public void importFromProfile(Profile profile) {
        EntityUtil.convert(profile, this);
        if (this.id != null && this.userId != null && !this.id.equals(this.userId)) {
            throw new SysException("用户信息错误");
        }
    }
}
