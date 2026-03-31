package fun.xianlai.system.iam.model.entity;

import fun.xianlai.system.iam.model.entity.pk.RolePermissionPK;
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
@Table(name = "tb_iam_role_permission")
@IdClass(RolePermissionPK.class)
public class RolePermission {
    @Id
    private Long roleId;
    @Id
    private Long permissionId;
}
