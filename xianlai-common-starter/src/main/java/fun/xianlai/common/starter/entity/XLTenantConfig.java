package fun.xianlai.common.starter.entity;

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
@Table(name = "tb_com_tenant_config", indexes = {
        @Index(columnList = "tenantId, configKey", unique = true)
})
public class XLTenantConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableGenerator(name = "pkGenTenantConfig", initialValue = 100000, allocationSize = 1)
    @Comment("主键ID")
    private Long id;

    /**
     * 取值见：EConfigScope
     * SYSTEM --> 只允许系统加载使用，租户、用户不允许加载使用
     * TENANT --> 允许系统、租户加载使用，用户不允许加载使用，默认值由系统维护，租户可覆盖此默认值
     * USER   --> 允许系统、租户、用户加载使用，默认值由系统维护，租户可覆盖此默认值，用户可覆盖租户的默认值，用户也可直接覆盖系统的默认值
     */
    @Column(columnDefinition = "varchar(10) not null")
    @Comment("作用域")
    private String scope;

    @Column(columnDefinition = "bigint not null")
    @Comment("作用域ID")
    private Long scopeId;   // XLTenantConfig 取值 tenantId

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
