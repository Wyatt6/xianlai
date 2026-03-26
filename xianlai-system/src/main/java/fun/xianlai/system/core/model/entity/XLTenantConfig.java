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
@Table(name = "tb_core_tenant_config", indexes = {
        @Index(columnList = "tenantId, configKey", unique = true)
})
public class XLTenantConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableGenerator(name = "pkGenTenantConfig", initialValue = 1, allocationSize = 1)
    @Comment("主键ID")
    private Long id;

    @Column(columnDefinition = "bigint not null")
    @Comment("租户ID")
    private Long tenantId;

    @Column(length = 100, nullable = false)
    @Comment("租户配置key")
    private String configKey;

    @Column(columnDefinition = "longtext")
    @Comment("租户配置值")
    private String configValue;

    @Column(columnDefinition = "bit not null default 0")
    @Comment("租户配置生效标志")
    private Boolean enabled;

    @Column(length = 50)
    @Comment("租户配置名称")
    private String name;

    @Column(length = 300)
    @Comment("租户配置备注")
    private String remark;

    @Column(columnDefinition = "datetime not null default current_timestamp")
    @Comment("记录创建时间")
    private LocalDateTime createAt;

    @Column(columnDefinition = "datetime not null default current_timestamp on update current_timestamp")
    @Comment("记录更新时间")
    private LocalDateTime updateAt;

    @PrePersist
    public void prePersist() {
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
}
