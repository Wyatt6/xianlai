package fun.xianlai.system.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 接口
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_core_api", indexes = {
        @Index(columnList = "callPath", unique = true)
})
public class XLApi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableGenerator(name="pkGenApi", initialValue = 100000, allocationSize = 1)
    private Long id;

    @Column(columnDefinition = "varchar(1000) not null")
    private String callPath;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) not null")
    private RequestMethod requestMethod;

    @Column(columnDefinition = "varchar(1000) not null")
    private String url;
}
