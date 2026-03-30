package fun.xianlai.system.core.entity;

import fun.xianlai.common.starter.pojo.XLTenantPOJO;
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
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

/**
 * @author WyattLau
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_com_tenant", indexes = {
        @Index(columnList = "code", unique = true),
        @Index(columnList = "domain", unique = true),
        @Index(columnList = "status")
})
public class XLTenant extends XLTenantPOJO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableGenerator(name = "pkGenTenant", initialValue = 100000, allocationSize = 1)
    @Comment("主键ID")
    @Override
    public Long getId() {
        return super.getId();
    }

    @Column(length = 100, nullable = false)
    @Comment("租户编码（唯一）")
    @Override
    public String getCode() {
        return super.getCode();
    }

    @Column(length = 300)
    @Comment("租户独立域名")
    @Override
    public String getDomain() {
        return super.getDomain();
    }

    @Column(columnDefinition = "varchar(30) not null default 'disabled'")
    @Comment("租户状态")
    @Override
    public String getStatus() {
        return super.getStatus();
    }

    @Column
    @Comment("租户过期时间")
    @Override
    public LocalDateTime getExpireTime() {
        return super.getExpireTime();
    }

    @Column(columnDefinition = "datetime not null default current_timestamp")
    @Comment("租户配置最后更新时间")
    @Override
    public LocalDateTime getConfigUpdateTime() {
        return super.getConfigUpdateTime();
    }

    @Column(length = 300)
    @Comment("租户Logo")
    @Override
    public String getLogo() {
        return super.getLogo();
    }

    @Column(length = 100, nullable = false)
    @Comment("租户显示名称")
    @Override
    public String getDisplayName() {
        return super.getDisplayName();
    }

    @Column(length = 50)
    @Comment("租户联系人")
    @Override
    public String getContactName() {
        return super.getContactName();
    }

    @Column(columnDefinition = "varchar(10) not null default 'UNKNOWN'")
    @Comment("租户联系人性别")
    @Override
    public String getContactGender() {
        return super.getContactGender();
    }

    @Column(length = 50)
    @Comment("租户联系人手机号")
    @Override
    public String getContactPhone() {
        return super.getContactPhone();
    }

    @Column(length = 100)
    @Comment("租户联系人电子邮箱")
    @Override
    public String getContactEmail() {
        return super.getContactEmail();
    }

    @Column(columnDefinition = "datetime not null default current_timestamp")
    @Comment("记录创建时间")
    @Override
    public LocalDateTime getCreateAt() {
        return super.getCreateAt();
    }

    @Column(columnDefinition = "datetime not null default current_timestamp on update current_timestamp")
    @Comment("记录更新时间")
    @Override
    public LocalDateTime getUpdateAt() {
        return super.getUpdateAt();
    }

    @PrePersist
    public void prePersist() {
        if (getConfigUpdateTime() == null) {
            setConfigUpdateTime(LocalDateTime.now());
        }
        if (getCreateAt() == null) {
            setCreateAt(LocalDateTime.now());
        }
        if (getUpdateAt() == null) {
            setUpdateAt(LocalDateTime.now());
        }
    }

    @PreUpdate
    public void preUpdate() {
        setUpdateAt(LocalDateTime.now());
    }

    public void refreshConfigUpdateTime() {
        setConfigUpdateTime(LocalDateTime.now());
    }
}
