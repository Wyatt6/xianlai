package fun.xianlai.app.iam.model.entity.other;

import fun.xianlai.app.iam.model.entity.rbac.User;
import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.utils.BeanUtils;
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
    private String avatar;
    private String nickname;
    private String photo;
    private String name;
    private String gender;
    private String employeeNo;
    private String phone;
    private String email;

    public User getUser() {
        User user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }

    public void setUser(User user) {
        if (this.id != null && !this.id.equals(user.getId())) {
            throw new SysException("用户信息错误");
        } else {
            BeanUtils.copyProperties(user, this);
        }
    }

    public Profile getProfile() {
        Profile profile = new Profile();
        BeanUtils.copyProperties(this, profile);
        profile.setUserId(this.id);
        return profile;
    }

    public void setProfile(Profile profile) {
        if (this.id != null && !this.id.equals(profile.getUserId())) {
            throw new SysException("用户信息错误");
        } else {
            BeanUtils.copyProperties(profile, this);
            this.id = profile.getUserId();
        }
    }
}
