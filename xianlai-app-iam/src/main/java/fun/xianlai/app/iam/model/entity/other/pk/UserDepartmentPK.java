package fun.xianlai.app.iam.model.entity.other.pk;

import lombok.Data;

import java.io.Serializable;

/**
 * @author WyattLau
 */
@Data
public class UserDepartmentPK implements Serializable {
    private Long userId;
    private Long departmentId;
}
