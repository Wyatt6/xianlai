package fun.xianlai.microservice.iam.model.entity.rbac;

import fun.xianlai.microservice.iam.model.entity.rbac.pk.GroupPermissionPK;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_iam_group_permission")
@IdClass(GroupPermissionPK.class)
public class GroupPermission {
    @Id
    private Long groupId;
    @Id
    private Long permissionId;
}
