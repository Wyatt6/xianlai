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
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

/**
 * 参数实体类
 * 把系统参数、用户自定义参数等都统一起来了，用optionType属性区分
 * 参数Key遵循规范：
 *      系统参数： sys.xxx.xxx
 *      用户参数： user.USER_ID.xxx.xxx
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
        @Index(columnList = "optionKey", unique = true),
        @Index(columnList = "optionType, sortId, optionKey"),   // optionType ASC, sortId ASC, optionKey ASC
        @Index(columnList = "frontLoad")
})
public class SysOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "pkGen")
    @GenericGenerator(name = "pkGen", type = PrimaryKeyGenerator.class)
    @Comment("主键ID")
    private Long id;

    @Column(columnDefinition = "bigint not null default 1000")
    @Comment("排序号，越小越前")
    private Long sortId;

    @Column(columnDefinition = "varchar(30) not null")
    @Comment("参数类型，见: EnumOptionType")
    private String optionType;

    @Column(columnDefinition = "varchar(100) not null")
    @Comment("参数Key（唯一）")
    private String optionKey;

    @Column(columnDefinition = "varchar(5000) not null")
    @Comment("参数值")
    private String optionValue;

    @Column(columnDefinition = "varchar(30) not null")
    @Comment("参数值类型，见: EnumOptionValueType")
    private String valueType;

    @Column(length = 5000)
    @Comment("参数值默认值")
    private String defaultValue;

    @Column(length = 50)
    @Comment("参数名")
    private String name;

    @Column(length = 500)
    @Comment("参数说明")
    private String description;

    @Column(columnDefinition = "bit not null default 0")
    @Comment("是否加载到前端")
    private Boolean frontLoad;
}
