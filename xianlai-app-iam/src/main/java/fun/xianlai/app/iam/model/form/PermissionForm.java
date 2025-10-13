package fun.xianlai.app.iam.model.form;

import fun.xianlai.app.iam.model.entity.rbac.Permission;
import lombok.Data;

import java.util.Date;

/**
 * @author WyattLau
 */
@Data
public class PermissionForm {
    private Long id;                // 主键
    private String identifier;      // 标识符
    private String name;            // 名称
    private String description;     // 说明
    private Long sortId;            // 排序号

    public Permission convert() {
        Permission result = new Permission();
        result.setId(id);
        result.setIdentifier(identifier != null ? identifier.trim() : null);
        result.setName(name != null ? name.trim() : null);
        result.setDescription(description != null ? description.trim() : null);
        result.setSortId(sortId);
        return result;
    }
}
