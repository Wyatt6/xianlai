package fun.xianlai.app.common.model.entity;

import fun.xianlai.app.common.support.PrimaryKeyGenerator;
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
 * 系统菜单
 * <p>
 * 用于控制菜单栏中显示的菜单
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_common_sys_menu", indexes = {
        @Index(columnList = "sortId")
})
public class SysMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PK_generator")
    @GenericGenerator(name = "PK_generator", type = PrimaryKeyGenerator.class)
    private Long id;

    @Column(columnDefinition = "bigint not null default 0")
    private Long sortId;

    @Column
    private String icon;

    @Column
    private String title;

    @Column(columnDefinition = "varchar(255) not null")
    private String pathName;    // SysPath的name

    @Column
    private String permission;

    @Column(columnDefinition = "bit not null default 0")
    private Boolean active;

    @Column(columnDefinition = "bigint not null default 0")
    private Long parentId;
}
