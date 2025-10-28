package fun.xianlai.app.iam.model.entity.rbac;

import fun.xianlai.core.supprt.PrimaryKeyGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

/**
 * RBAC-用户
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_iam_user", indexes = {
        @Index(columnList = "username", unique = true)
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "pkGen")
    @GenericGenerator(name = "pkGen", type = PrimaryKeyGenerator.class)
    private Long id;

    @Column(columnDefinition = "varchar(100) not null")
    private String username;

    @Column(columnDefinition = "varchar(1000) not null")
    private String password;

    @Column(columnDefinition = "varchar(100) not null")
    private String salt;                // 加密盐

    @Column
    private Date registerTime;          // 注册时间

    @Column(columnDefinition = "bit not null default 0")
    private Boolean active;

    @Column(columnDefinition = "bit not null default 0")
    private Boolean isDelete;

    // ----- 非持久化属性 -----
    @Transient
    private String captchaKey;  // 验证码KEY

    @Transient
    private String captcha;     // 验证码

    public User(Long id, String username, String password, String salt, Date registerTime, Boolean active, Boolean isDelete) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.registerTime = registerTime;
        this. active = active;
        this.isDelete = isDelete;
    }
}
