package fun.xianlai.app.iam.model.form;

import fun.xianlai.app.iam.model.entity.rbac.Role;
import lombok.Data;

import java.util.Date;

/**
 * @author WyattLau
 */
@Data
public class RoleForm {
    private Long id;            // 主键
    private String identifier;  // 标识符
    private String name;        // 名称
    private Boolean active;     // 启用/禁用
    private String description; // 说明
    private Long sortId;        // 排序号

    public Role convert() {
        Role result = new Role();
        result.setId(id);
        result.setIdentifier(identifier != null ? identifier.trim() : null);
        result.setName(name != null ? name.trim() : null);
        result.setActive(active);
        result.setDescription(description != null ? description.trim() : null);
        result.setSortId(sortId);
        return result;
    }
}
