package fun.xianlai.system.iam.model.entity.pk;

import lombok.Data;

import java.io.Serializable;

/**
 * @author WyattLau
 */
@Data
public class RolePermissionPK implements Serializable {
    private Long roleId;
    private Long permissionId;
}
