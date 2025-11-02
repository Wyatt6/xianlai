package fun.xianlai.app.iam.model.form;

import fun.xianlai.app.iam.model.entity.rbac.Permission;
import fun.xianlai.core.utils.StringUtil;
import lombok.Data;

/**
 * @author WyattLau
 */
@Data
public class PermissionForm {
    private Long id;
    private Long sortId;
    private String identifier;
    private String name;
    private String description;

    public Permission convert() {
        Permission result = new Permission();
        result.setId(id);
        result.setSortId(sortId);
        result.setIdentifier(StringUtil.trim(identifier));
        result.setName(StringUtil.trim(name));
        result.setDescription(StringUtil.trim(description));
        return result;
    }
}
