package fun.xianlai.app.iam.model.form;

import fun.xianlai.app.iam.model.entity.rbac.Role;
import lombok.Data;

/**
 * @author WyattLau
 */
@Data
public class RoleForm {
    private Long id;            // 主键
    private Long sortId;        // 排序号
    private String identifier;  // 标识符
    private String name;        // 名称
    private String description; // 说明
    private Boolean active;     // 启用/禁用
    private Boolean bindCheck;  // 用户绑定本角色时是否需要检查有无权限

    public Role convert() {
        Role result = new Role();
        result.setId(id);
        result.setSortId(sortId);
        result.setIdentifier(identifier != null ? identifier.trim() : null);
        result.setName(name != null ? name.trim() : null);
        result.setDescription(description != null ? description.trim() : null);
        result.setActive(active);
        result.setBindCheck(bindCheck);
        return result;
    }
}
