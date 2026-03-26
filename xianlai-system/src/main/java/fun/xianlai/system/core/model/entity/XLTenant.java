package fun.xianlai.system.core.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

/**
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_core_tenant", indexes = {
        @Index(columnList = "code", unique = true),
        @Index(columnList = "domain", unique = true),
        @Index(columnList = "status")
})
public class XLTenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableGenerator(name = "pkGenTenant", initialValue = 100000, allocationSize = 1)
    @Comment("主键ID")
    private Long id;

    @Column(length = 100, nullable = false)
    @Comment("租户编码（唯一）")
    private String code;

    @Column(length = 300)
    @Comment("租户独立域名")
    private String domain;

    @Column(columnDefinition = "varchar(30) not null default 'disabled'")
    @Comment("租户状态")
    private String status;

    @Column
    @Comment("租户过期时间")
    private LocalDateTime expireTime;

    @Column(columnDefinition = "datetime not null default current_timestamp")
    @Comment("租户配置最后更新时间")
    private LocalDateTime configUpdateTime;

    @Column(length = 300)
    @Comment("租户Logo")
    private String logo;

    @Column(length = 100, nullable = false)
    @Comment("租户显示名称")
    private String displayName;

    @Column(length = 50)
    @Comment("租户联系人")
    private String contactName;

    @Column(columnDefinition = "varchar(10) not null default 'UNKNOWN'")
    @Comment("租户联系人性别")
    private String contactGender;

    @Column(length = 50)
    @Comment("租户联系人手机号")
    private String contactPhone;

    @Column(length = 100)
    @Comment("租户联系人电子邮箱")
    private String contactEmail;

    @Column(columnDefinition = "datetime not null default current_timestamp")
    @Comment("记录创建时间")
    private LocalDateTime createAt;

    @Column(columnDefinition = "datetime not null default current_timestamp on update current_timestamp")
    @Comment("记录更新时间")
    private LocalDateTime updateAt;

    @PrePersist
    public void prePersist() {
        if (this.configUpdateTime == null) {
            this.configUpdateTime = LocalDateTime.now();
        }
        if (this.createAt == null) {
            this.createAt = LocalDateTime.now();
        }
        if (this.updateAt == null) {
            this.updateAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updateAt = LocalDateTime.now();
    }

    public void refreshConfigUpdateTime() {
        this.configUpdateTime = LocalDateTime.now();
    }
}
