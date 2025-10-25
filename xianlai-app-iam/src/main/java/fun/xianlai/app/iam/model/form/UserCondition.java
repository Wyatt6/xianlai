package fun.xianlai.app.iam.model.form;

import lombok.Data;

import java.util.Date;

@Data
public class UserCondition {
    private String username;        // 用户名
    private Date stRegisterTime;    // 注册时间开始
    private Date edRegisterTime;    // 注册时间结束
    private Boolean active;         // 启用/禁用
    private String role;            // 包含角色
    private String permission;      // 包含权限
}
