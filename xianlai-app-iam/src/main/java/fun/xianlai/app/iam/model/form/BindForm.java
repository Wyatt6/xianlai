package fun.xianlai.app.iam.model.form;

import lombok.Data;

import java.util.List;

/**
 * @author WyattLau
 */
@Data
public class BindForm {
    private Long userId;
    private List<Long> bind;        // 绑定ID列表
    private List<Long> cancel;      // 取消绑定ID列表
}
