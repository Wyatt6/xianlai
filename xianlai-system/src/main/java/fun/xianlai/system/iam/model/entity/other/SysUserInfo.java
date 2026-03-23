package fun.xianlai.system.iam.model.entity.other;

import fun.xianlai.system.iam.model.entity.SysUser;
import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.utils.bean.BeanUtils;
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
public class SysUserInfo {
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

    public SysUser exportUser() {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(this, user);
        return user;
    }

    public void importUser(SysUser user) {
        if (this.id != null && !this.id.equals(user.getId())) {
            throw new SysException("用户信息错误");
        } else {
            BeanUtils.copyProperties(user, this);
        }
    }

    public SysProfile exportProfile() {
        SysProfile profile = new SysProfile();
        BeanUtils.copyProperties(this, profile);
        profile.setUserId(this.id);
        return profile;
    }

    public void importProfile(SysProfile profile) {
        if (this.id != null && !this.id.equals(profile.getUserId())) {
            throw new SysException("用户信息错误");
        } else {
            BeanUtils.copyProperties(profile, this);
            this.id = profile.getUserId();
        }
    }
}
