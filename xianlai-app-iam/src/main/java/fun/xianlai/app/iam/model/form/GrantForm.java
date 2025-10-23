package fun.xianlai.app.iam.model.form;

import lombok.Data;

import java.util.List;

/**
 * @author WyattLau
 */
@Data
public class GrantForm {
    private Long roleId;
    private List<Long> grant;       // 授权ID列表
    private List<Long> cancel;      // 取消授权ID列表
}
