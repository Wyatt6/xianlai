package fun.xianlai.app.iam.model.form;

import fun.xianlai.app.iam.model.entity.other.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserCondition extends UserInfo {
    private Date stRegisterTime;    // 注册时间开始
    private Date edRegisterTime;    // 注册时间结束
    private String role;            // 包含角色
    private String permission;      // 包含权限
}
