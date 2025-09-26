package fun.xianlai.microservice.iam.model.entity.rbac.pk;

import lombok.Data;

import java.io.Serializable;

/**
 * @author WyattLau
 */
@Data
public class GroupPermissionPK implements Serializable {
    private Long groupId;
    private Long permissionId;
}
