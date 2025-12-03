package fun.xianlai.app.iam.model.entity.rbac.pk;

import lombok.Data;

import java.io.Serializable;

/**
 * @author WyattLau
 */
@Data
public class UserRolePK implements Serializable {
    private Long userId;
    private Long roleId;
}
