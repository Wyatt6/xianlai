package fun.xianlai.app.iam.model.entity.rbac.pk;

import lombok.Data;

import java.io.Serializable;

/**
 * @author WyattLau
 */
@Data
public class GroupRolePK implements Serializable  {
    private Long groupId;
    private Long roleId;
}
