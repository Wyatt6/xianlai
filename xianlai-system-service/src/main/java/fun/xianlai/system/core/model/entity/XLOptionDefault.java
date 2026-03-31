package fun.xianlai.system.core.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 默认参数
 * 参数Key规范：
 * system.xxx.xxx.……
 * tenant.{0}.xxx.xxx.……
 * user.{0}.xxx.xxx.……
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_common_option_default", indexes = {
        @Index(columnList = "optionKey", unique = true),
        @Index(columnList = "scope, optionKey"),   // scope ASC, optionKey ASC
        @Index(columnList = "frontLoad")
})
public class XLOptionDefault {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableGenerator(name="optionDefaultPkGen", initialValue = 1000000, allocationSize = 1)
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
