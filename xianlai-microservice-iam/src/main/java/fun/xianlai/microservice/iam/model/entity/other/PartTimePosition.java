package fun.xianlai.microservice.iam.model.entity.other;

import fun.xianlai.microservice.iam.model.entity.other.pk.UserPositionPK;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户的兼任职务/岗位
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_iam_part_time_position")
@IdClass(UserPositionPK.class)
public class PartTimePosition {
    @Id
    private Long userId;
    @Id
    private Long positionId;
}
