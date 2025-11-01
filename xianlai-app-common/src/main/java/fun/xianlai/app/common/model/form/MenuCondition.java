package fun.xianlai.app.common.model.form;

import lombok.Data;

/**
 * @author WyattLau
 */
@Data
public class MenuCondition {
    private Long parentId;
    private String title;
    private String pathName;    // SysPath的name
    private Boolean needPermission;
    private String permission;  // needPermission = true 时才有意义
    private Boolean active;
}