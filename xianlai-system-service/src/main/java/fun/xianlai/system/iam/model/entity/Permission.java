package fun.xianlai.system.iam.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_iam_permission", indexes = {
        @Index(columnList = "tenantId, identifier", unique = true),
        @Index(columnList = "tenantId, sortId, identifier")
})
public class Permission {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "pkGen")
//    @GenericGenerator(name = "pkGen", type = PrimaryKeyGenerator.class)
    private Long id;

    @Column(columnDefinition = "bigint not null")
    private Long tenantId;

    @Column(columnDefinition = "bigint not null default 100")
    private Long sortId;

    @Column(columnDefinition = "varchar(300) not null")
    private String identifier;

    @Column
    private String name;

    @Column(length = 1000)
    private String description;
}
