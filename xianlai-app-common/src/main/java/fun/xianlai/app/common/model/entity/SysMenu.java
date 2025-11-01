package fun.xianlai.app.common.model.entity;

import fun.xianlai.core.supprt.PrimaryKeyGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "pkGen")
    @GenericGenerator(name = "pkGen", type = PrimaryKeyGenerator.class)
    private Long id;

    @Column(columnDefinition = "bigint not null default 0")
    private Long sortId;

    @Column(columnDefinition = "bigint not null default 0")
    private Long parentId;

    @Column
    private String icon;

    @Column(columnDefinition = "varchar(255) not null")
    private String title;

    @Column(columnDefinition = "varchar(255) not null")
    private String pathName;    // SysPath的name

    @Column(columnDefinition = "bit not null default 0")
    private Boolean needPermission;

    @Column
    private String permission;  // needPermission = true 时才有意义

    @Column(columnDefinition = "bit not null default 0")
    private Boolean active;

    // ----- 非持久化属性 -----
    @Transient
    private List<SysMenu> children = new ArrayList<>();

    public SysMenu(Long id, Long sortId, Long parentId, String icon, String title, String pathName, Boolean needPermission, String permission, Boolean active) {
        this.id = id;
        this.sortId = sortId;
        this.parentId = parentId;
        this.icon = icon;
        this.title = title;
        this.pathName = pathName;
        this.needPermission = needPermission;
        this.permission = permission;
        this.active = active;
    }
}
