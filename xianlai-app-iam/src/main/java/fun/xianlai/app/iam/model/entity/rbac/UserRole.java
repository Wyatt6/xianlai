package fun.xianlai.app.iam.model.entity.rbac;

import fun.xianlai.app.iam.model.entity.rbac.pk.UserRolePK;
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
@Table(name = "tb_iam_user_role")
@IdClass(UserRolePK.class)
public class UserRole {
    @Id
    private Long userId;
    @Id
    private Long roleId;
}
