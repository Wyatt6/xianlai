package fun.xianlai.app.common.model.entity;

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
 * 系统路径
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
@Table(name = "tb_common_sys_path", indexes = {
        @Index(columnList = "path", unique = true),
        @Index(columnList = "sortId")
})
public class SysPath {
    @Id
    private String name;

    @Column(columnDefinition = "varchar(1024) not null")
    private String path;

    @Column(columnDefinition = "bigint not null default 0")
    private Long sortId;
}
