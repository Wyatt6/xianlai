package fun.xianlai.system.service.iam.model.entity.other;

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
    private Long userId;

    @Column
    private String avatar;      // 头像文件名

    @Column
    private String nickname;

    @Column(columnDefinition = "varchar(20) default 'unknown'")
    private String gender;

    @Column
    private String phone;

    @Column
    private String email;
}
