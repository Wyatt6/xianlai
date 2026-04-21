package fun.xianlai.system.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 全局配置版本
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_core_config_version")
public class XLConfigVersion implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Comment("主键ID")
    private Long id;

    @Column(columnDefinition = "bigint not null default 0")
    @Comment("全局配置版本")
    private Long globalVersion;

    @Column(columnDefinition = "datetime(3) not null default current_timestamp(3) on update current_timestamp(3)")
    @Comment("更新时间")
    private LocalDateTime updateAt;

    @PreUpdate
    public void preUpdate() {
        this.updateAt = LocalDateTime.now();
    }
}
