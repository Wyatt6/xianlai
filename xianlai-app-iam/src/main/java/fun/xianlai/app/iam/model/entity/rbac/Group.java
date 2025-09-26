package fun.xianlai.app.iam.model.entity.rbac;

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

/**
 * RBAC-组
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_iam_group", indexes = {
        @Index(columnList = "identifier", unique = true),
        @Index(columnList = "name")
})
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PK_generator")
    @GenericGenerator(name = "PK_generator", type = PrimaryKeyGenerator.class)
    private Long id;

    @Column(columnDefinition = "varchar(255) not null")
    private String identifier;

    @Column
    private String name;

    @Column(columnDefinition = "bit not null default 0")
    private Boolean active;

    @Column(length = 1024)
    private String description;

    @Column(columnDefinition = "bigint not null default 0")
    private Long sortId;
}
