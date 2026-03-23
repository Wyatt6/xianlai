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
 * 参数定义
 * <p>
 * 把系统参数、用户自定义参数等都统一起来了，用type属性区分
 * 参数Key应遵循规范：
 * 系统参数： sys.xxx.xxx
 * 租户参数： tenant.TENANT_ID.xxx.xxx
 * 用户参数： user.USER_ID.xxx.xxx
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_common_option_definition", indexes = {
        @Index(columnList = "optionKey", unique = true),
        @Index(columnList = "type, optionKey"),   // type ASC, optionKey ASC
        @Index(columnList = "frontLoad")
})
public class SysOptionDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "pkGen")
    @GenericGenerator(name = "pkGen", type = PrimaryKeyGenerator.class)
    private Long id;

    @Column(columnDefinition = "varchar(30) not null")
    private String scope;           // 参数作用域，取值见： EnumOptionScope

    @Column(columnDefinition = "varchar(100) not null")
    private String optionKey;

    @Column(length = 8000)
    private String defaultValue;    // 参数值默认值

    @Column(columnDefinition = "varchar(30) not null")
    private String valueType;       // 参数值类型，取值见： EnumOptionValueType

    @Column(length = 50)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(columnDefinition = "bit not null default 0")
    private Boolean frontLoad;      // 是否加载到前端
}
