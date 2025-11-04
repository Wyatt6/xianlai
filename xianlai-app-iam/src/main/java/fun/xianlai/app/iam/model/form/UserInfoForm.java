package fun.xianlai.app.iam.model.form;

import fun.xianlai.app.iam.model.entity.other.Profile;
import fun.xianlai.app.iam.model.entity.rbac.User;
import lombok.Data;

/**
 * @author WyattLau
 */
@Data
public class UserInfoForm {
    private Long id;
    private String username;
    private Boolean active;
    private Boolean isDelete;

    public User extractToUser() {
        User result = new User();
        result.setId(id);
        result.setUsername(username != null ? username.trim() : null);
        result.setActive(active);
        result.setIsDelete(isDelete);
        return result;
    }

    public void importFromUser(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.active = user.getActive();
        this.isDelete = user.getIsDelete();
    }

    public Profile extractToProfile() {
        return null;
    }
}
