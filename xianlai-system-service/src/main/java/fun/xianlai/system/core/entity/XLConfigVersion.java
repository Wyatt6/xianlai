package fun.xianlai.system.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * 配置版本号
 * 只有 tb_core_config 中 belongId=0 的配置有修改时才需要更新此版本号
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
    @Comment("版本号")
    private Long version;

    @Column(columnDefinition = "datetime(3) not null default current_timestamp(3) on update current_timestamp(3)")
    @Comment("更新时间")
    private LocalDateTime updateAt;

    @PreUpdate
    public void preUpdate() {
        this.updateAt = LocalDateTime.now();
    }
}
