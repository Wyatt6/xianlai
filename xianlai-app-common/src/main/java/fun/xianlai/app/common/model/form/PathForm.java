package fun.xianlai.app.common.model.form;

import fun.xianlai.app.common.model.entity.SysPath;
import fun.xianlai.core.utils.StringUtil;
import lombok.Data;

/**
 * @author WyattLau
 */
@Data
public class PathForm {
    private Long id;
    private Long sortId;
    private String name;
    private String path;

    public SysPath convert() {
        SysPath result = new SysPath();
        result.setId(id);
        result.setSortId(sortId);
        result.setName(StringUtil.trim(name));
        result.setPath(StringUtil.trim(path));
        return result;
    }
}
