package fun.xianlai.system.iam.model.entity.pk;

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
