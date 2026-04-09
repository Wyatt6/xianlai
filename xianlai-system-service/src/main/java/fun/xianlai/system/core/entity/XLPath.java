package fun.xianlai.system.core.entity;

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

import java.io.Serial;
import java.io.Serializable;

/**
 * 路径
 * <p>
 * 用于注册各个页面访问URL的路径，例如：
 * PORTAL: '/portal',
 * LOGIN: '/portal/login',
 * REGISTER: '/portal/register',
 * RESET_PASSWORD: '/portal/reset-password'
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_core_path", indexes = {
        @Index(columnList = "name", unique = true),
        @Index(columnList = "path", unique = true),
        @Index(columnList = "sortId, name")
})
public class XLPath implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableGenerator(name = "pkGenPath", initialValue = 100000, allocationSize = 1)
    private Long id;

    @Column(columnDefinition = "bigint not null default 1000")
    private Long sortId;

    @Column(columnDefinition = "varchar(200) not null")
    private String name;

    @Column(columnDefinition = "varchar(1000) not null")
    private String path;
}
