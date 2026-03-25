package fun.xianlai.system.iam.model.entity.other;

import fun.xianlai.system.iam.model.entity.User;
import fun.xianlai.common.exception.SysException;
import fun.xianlai.common.utils.bean.BeanUtils;
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
    private Long tenantId;
    private String username;
    private Date registerAt;
    private Boolean active;
    private Boolean isDeleted;
    // Profile
    private String avatar;
    private String nickname;
    private String gender;
    private String phone;
    private String email;

    public User exportUser() {
        User user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }

    public void importUser(User user) {
        if (this.id != null && !this.id.equals(user.getId())) {
            throw new SysException("用户信息错误");
        } else {
            BeanUtils.copyProperties(user, this);
        }
    }

    public Profile exportProfile() {
        Profile profile = new Profile();
        BeanUtils.copyProperties(this, profile);
        profile.setUserId(this.id);
        return profile;
    }

    public void importProfile(Profile profile) {
        if (this.id != null && !this.id.equals(profile.getUserId())) {
            throw new SysException("用户信息错误");
        } else {
            BeanUtils.copyProperties(profile, this);
            this.id = profile.getUserId();
        }
    }
}
