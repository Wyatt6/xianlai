package fun.xianlai.app.iam.model.entity.rbac;

import fun.xianlai.app.iam.model.entity.rbac.pk.GroupRolePK;
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
@Table(name = "tb_iam_group_role")
@IdClass(GroupRolePK.class)
public class GroupRole {
    @Id
    private Long groupId;
    @Id
    private Long roleId;
}
