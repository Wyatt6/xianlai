package fun.xianlai.system.iam.model.entity;

import fun.xianlai.core.utils.bean.PrimaryKeyGenerator;
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
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_iam_user", indexes = {
        @Index(columnList = "tenantId, username", unique = true)
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "pkGen")
    @GenericGenerator(name = "pkGen", type = PrimaryKeyGenerator.class)
    private Long id;

    @Column(columnDefinition = "bigint not null")
    private Long tenantId;

    @Column(columnDefinition = "varchar(100) not null")
    private String username;

    @Column(columnDefinition = "varchar(1000) not null")
    private String password;

    @Column(columnDefinition = "varchar(100) not null")
    private String salt;                // 加密盐

    @Column
    private Date registerAt;          // 注册时间

    @Column(columnDefinition = "bit not null default 0")
    private Boolean active;

    @Column(columnDefinition = "bit not null default 0")
    private Boolean isDeleted;

    // ----- 非持久化属性 -----
    @Transient
    private String captchaKey;  // 验证码KEY

    @Transient
    private String captcha;     // 验证码

    public User(Long id, Long tenantId, String username, String password, String salt, Date registerAt, Boolean active, Boolean isDeleted) {
        this.id = id;
        this.tenantId = tenantId;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.registerAt = registerAt;
        this.active = active;
        this.isDeleted = isDeleted;
    }
}
