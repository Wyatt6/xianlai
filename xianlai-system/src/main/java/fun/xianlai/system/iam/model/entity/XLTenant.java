package fun.xianlai.system.iam.model.entity;

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
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_iam_tenant", indexes = {
        @Index(columnList = "code", unique = true)
})
public class XLTenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableGenerator(name="tenantPkGen", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(columnDefinition = "varchar(50) not null")
    private String code;

    @Column(columnDefinition = "varchar(100) not null")
    private String name;

    @Column(columnDefinition = "bit not null default 0")
    private String active;
}
