package fun.xianlai.app.iam.model.entity.other;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_iam_profile", indexes = {
        @Index(columnList = "nickname", unique = true),
        @Index(columnList = "phone", unique = true),
        @Index(columnList = "email", unique = true)
})
public class Profile {
    @Id
    private Long userId;    // 和User实体主键id一致

    @Column
    private String avatar;              // 头像（文件名）

    @Column
    private String nickname;

    @Column
    private String gender;              // 性别

    @Column
    private String phone;

    @Column
    private String email;
}
