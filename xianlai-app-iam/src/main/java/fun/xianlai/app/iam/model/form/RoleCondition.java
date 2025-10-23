package fun.xianlai.app.iam.model.form;

import lombok.Data;

/**
 * @author WyattLau
 */
@Data
public class RoleCondition {
    private String identifier;      // 标识符
    private String name;            // 名称
    private String description;     // 说明
    private Boolean active;         // 启用/禁用
    private String permission;      // 包含权限
}
