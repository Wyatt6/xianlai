package fun.xianlai.app.common.model.entity;

import fun.xianlai.app.common.model.enums.JsType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 系统参数
 * <p>
 * 影响系统整体运行的参数，以键值对的形式存储
 * 通常由系统在初始化时自动生成，并由管理员管理
 * 只允许修改参数值，不允许修改其他属性
 * 区别于用于保存系统用户个性化设置的用户参数
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_common_sys_option", indexes = {
        @Index(columnList = "sortId"),
        @Index(columnList = "name")
})
public class SysOption {
    @Id
    private String optionKey;           // 参数键

    @Column(columnDefinition = "varchar(7000) not null")
    private String optionValue;         // 参数值

    @Column(length = 7000)
    private String defaultValue;        // 默认参数值

    @Column(columnDefinition = "bigint not null default 0")
    private Long sortId;

    @Column
    private String name;

    @Column(length = 1024)
    private String description;

    @Column(columnDefinition = "bit not null default 0")
    private Boolean backLoad;           // 是否加载到后端

    @Column(columnDefinition = "bit not null default 0")
    private Boolean frontLoad;          // 是否加载到前端

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private JsType jsType;              // 参数值的js数据类型（frontLoad = true才有意义）
}
