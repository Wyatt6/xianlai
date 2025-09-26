package fun.xianlai.app.iam.model.entity.rbac.pk;

import lombok.Data;

import java.io.Serializable;

/**
 * @author WyattLau
 */
@Data
public class UserGroupPK implements Serializable {
    private Long userId;
    private Long groupId;
}
