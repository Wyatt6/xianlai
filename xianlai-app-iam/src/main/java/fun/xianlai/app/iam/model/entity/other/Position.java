package fun.xianlai.app.iam.model.entity.other;

import fun.xianlai.app.iam.model.support.PrimaryKeyGenerator;
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

import java.util.Date;

/**
 * 职务/岗位
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_iam_position", indexes = {
        @Index(columnList = "departmentId"),
        @Index(columnList = "name"),
        @Index(columnList = "departmentId,name", unique = true),
        @Index(columnList = "createTime"),
        @Index(columnList = "sortId")
})
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PK_generator")
    @GenericGenerator(name = "PK_generator", type = PrimaryKeyGenerator.class)
    private Long id;

    @Column(columnDefinition = "bigint not null")
    private Long departmentId;

    @Column(columnDefinition = "varchar(255) not null")
    private String name;

    @Column(columnDefinition = "bit not null default 0")
    private Boolean active;

    @Column(length = 1024)
    private String description;

    @Column
    private Date createTime;    // 职位/岗位设立时间（不是数据记录生成时间）

    @Column(columnDefinition = "bigint not null default 0")
    private Long sortId;
}
