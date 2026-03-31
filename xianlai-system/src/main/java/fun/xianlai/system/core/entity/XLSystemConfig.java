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
 * 【配置机制说明】
 * <p>
 * 三级别配置层级
 * 1. 系统配置 XLSystemConfig    tb_core_system_config
 * 2. 租户配置 XLTenantConfig    tb_core_tenant_config
 * 3. 用户配置 XLUserConfig      tb_iam_user_config
 * <p>
 * 配置覆盖关系
 * 1. 系统配置是最底层的配置，作为兜底的配置值，即默认值
 * 2. 系统配置中scope=TENANT/USER的项可以作为模板给每个新租户生成一套租户配置，或者被租户配置用懒加载的方式覆盖
 * 3. 系统配置中scope=USER的项可以作为模板每个新用户生成一套用户配置，或者被用户配置用懒加载的方式覆盖
 * 4. 要实现以上3点应满足：用户配置、租户配置的configKey可以在系统配置里找到，在初始化或管理用户配置和租户配置时，应基于系统配置数据而来
 * 5. 所以在缓存时默认了租户配置是符合能且仅能覆盖系统配置中scope=TENANT/USER的项，用户配置是能符合能且近能覆盖系统配置中scope=USER的项
 * <p>
 * 前端加载
 * (1) 是否能加载到前端与scope无关系
 * (2) 系统配置、租户配置、用户配置中frontLoad为true的就可以允许加载到前端
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_core_system_config", indexes = {
        @Index(columnList = "belongTo, configKey", unique = true),
        @Index(columnList = "enabled")
})
public class XLSystemConfig implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableGenerator(name = "pkGenSystemConfig", initialValue = 100000, allocationSize = 1)
    @Comment("主键ID")
    private Long id;

    @Column(columnDefinition = "bigint not null default 0")
    @Comment("配置归属")
    private Long belongTo;  // XLSystemConfig 恒为 0

    @Column(columnDefinition = "varchar(10) not null")
    @Comment("作用域")
    private String scope;    // 取值见：EConfigScope

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
