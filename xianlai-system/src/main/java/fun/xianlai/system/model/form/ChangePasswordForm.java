package fun.xianlai.system.model.form;

import lombok.Data;

@Data
public class ChangePasswordForm {
    private String oldPassword;     // 旧密码
    private String newPassword;     // 新密码
}
