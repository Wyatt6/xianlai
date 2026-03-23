package fun.xianlai.system.common.model.entity;

import fun.xianlai.core.utils.bean.PrimaryKeyGenerator;
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

/**
 * 参数
 * 一旦参数值修改后就用此记录的数据覆盖默认参数
 * 参数Key规范：
 * system.xxx.xxx.……
 * tenant.TENANT_ID.xxx.xxx.……
 * user.USER_ID.xxx.xxx.……
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_common_option", indexes = {
        @Index(columnList = "scope, scopeId, optionKey", unique = true), // scope ASC, scopeId ASC, optionKey ASC
        @Index(columnList = "frontLoad")
})
public class SysOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "pkGen")
    @GenericGenerator(name = "pkGen", type = PrimaryKeyGenerator.class)
    private Long id;

    @Column(columnDefinition = "varchar(30) not null")
    private String scope;           // 参数作用域，取值见： EnumOptionScope

    /**
     * 当 scope=SYSTEM 时，取值 0
     * 当 scope=TENANT 时，取值 tenantId
     * 当 scope=USER 时，取值 userId
     */
    @Column(columnDefinition = "bigint not null default 0")
    private Long scopeId;           // 参数作用域ID

    @Column(columnDefinition = "varchar(100) not null")
    private String optionKey;

    @Column(length = 8000)
    private String optionValue;     // 参数值

    @Column(columnDefinition = "varchar(30) not null")
    private String valueType;       // 参数值类型，取值见： EnumOptionValueType

    @Column(length = 50)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(columnDefinition = "bit not null default 0")
    private Boolean frontLoad;      // 是否加载到前端
}
