package fun.xianlai.system.core.entity;

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

import java.io.Serial;
import java.io.Serializable;
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
        @Index(columnList = "belongTo, configKey", unique = true),
        @Index(columnList = "belongTo, enabled")
})
public class XLTenantConfig implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableGenerator(name = "pkGenTenantConfig", initialValue = 100000, allocationSize = 1)
    @Comment("主键ID")
    private Long id;

    @Column(columnDefinition = "bigint not null")
    @Comment("配置归属")
    private Long belongTo;   // XLTenantConfig 取值 tenantId

    @Column(columnDefinition = "varchar(10) not null")
    @Comment("作用域")
    private String scope;   // 取值复制自XLSystemConfig，此处不产生意义，取值见：EConfigScope

    @Column(length = 100, nullable = false)
    @Comment("配置key")
    private String configKey;

    @Column(columnDefinition = "longtext")
    @Comment("配置值")
    private String configValue;

    @Column(columnDefinition = "varchar(30) not null")
    @Comment("配置值数据类型")
    private String valueType;       // 取值见： EConfigValueType

    @Column(columnDefinition = "bit not null default 0")
    @Comment("配置生效标志")
    private Boolean enabled;

    @Column(columnDefinition = "bit not null default 0")
    @Comment("配置前端加载标志")
    private Boolean frontLoad;

    @Column(length = 50)
    @Comment("配置名称")
    private String name;

    @Column(length = 300)
    @Comment("配置备注")
    private String remark;

    @Column(columnDefinition = "datetime not null default current_timestamp(3)")
    @Comment("记录创建时间")
    private LocalDateTime createAt;

    @Column(columnDefinition = "datetime not null default current_timestamp(3) on update current_timestamp(3)")
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
