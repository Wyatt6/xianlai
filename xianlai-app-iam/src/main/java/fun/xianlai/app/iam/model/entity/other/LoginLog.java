package fun.xianlai.app.iam.model.entity.other;

import fun.xianlai.core.supprt.PrimaryKeyGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

/**
 * 登录日志
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_iam_login_log", indexes = {
        @Index(columnList = "userId"),
        @Index(columnList = "loginTime"),
        @Index(columnList = "userId,loginTime")
})
public class LoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PK_generator")
    @GenericGenerator(name = "PK_generator", type = PrimaryKeyGenerator.class)
    private Long id;

    @Column(columnDefinition = "bigint not null")
    private Long userId;

    @Column(columnDefinition = "datetime not null")
    private Date loginTime;

    @Column
    private String ip;

    @Column
    private String address;

    @Column
    private String os;

    @Column
    private String browser;

    @Column(columnDefinition = "bit not null")
    private Boolean loginSuccess;

    @Column(length = 1024)
    private String message;
}
