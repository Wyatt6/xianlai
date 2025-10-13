package fun.xianlai.app.iam.model.entity.rbac;

import fun.xianlai.app.iam.support.PrimaryKeyGenerator;
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
 * RBAC-角色
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_iam_role", indexes = {
        @Index(columnList = "identifier", unique = true),
        @Index(columnList = "name"),
        @Index(columnList = "sortId")
})
public class Role {
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

    @Column(columnDefinition = "bit not null default 0")
    private Boolean active;

    @Column(columnDefinition = "bigint not null default 0")
    private Long sortId;
}
