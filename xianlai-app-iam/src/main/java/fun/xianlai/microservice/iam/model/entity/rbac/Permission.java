package fun.xianlai.microservice.iam.model.entity.rbac;

import fun.xianlai.microservice.iam.model.support.PrimaryKeyGenerator;
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
 * RBAC-权限
 * 由程序运行时自动生成，不允许修改
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_iam_permission", indexes = {
        @Index(columnList = "identifier", unique = true),
        @Index(columnList = "name"),
        @Index(columnList = "sortId")
})
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PK_generator")
    @GenericGenerator(name = "PK_generator", type = PrimaryKeyGenerator.class)
    private Long id;

    @Column(columnDefinition = "varchar(255) not null")
    private String identifier;

    @Column
    private String name;

    @Column(length = 1024)
    private String description;

    @Column(columnDefinition = "bigint not null default 0")
    private Long sortId;
}
