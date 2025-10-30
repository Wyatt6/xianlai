package fun.xianlai.app.common.model.form;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author WyattLau
 */
@Data
public class SysApiCondition {
    private String callPath;
    private String description;
    private RequestMethod requestMethod;
    private String url;
}
