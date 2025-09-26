package fun.xianlai.app.iam.model.entity.other;

import fun.xianlai.app.iam.model.entity.other.pk.UserDepartmentPK;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户的兼职部门
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_iam_part_time_department")
@IdClass(UserDepartmentPK.class)
public class PartTimeDepartment {
    @Id
    private Long userId;
    @Id
    private Long departmentId;
}
