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
 * 路由
 *
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_common_sys_route", indexes = {
        @Index(columnList = "name", unique = true),
        @Index(columnList = "sortId")
})
public class SysRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "pkGen")
    @GenericGenerator(name = "pkGen", type = PrimaryKeyGenerator.class)
    private Long id;

    @Column(columnDefinition = "bigint not null default 0")
    private Long sortId;

    @Column(columnDefinition = "bigint not null default 0")
    private Long parentId;

    @Column(columnDefinition = "varchar(255) not null")
    private String name;

    @Column(columnDefinition = "varchar(255) not null")
    private String pathName;            // SysPath的name

    @Column
    private String redirectPathName;    // SysPath的name

    @Column(length = 1000)
    private String componentPath;       // 组件（相对）路径

    @Column(columnDefinition = "bit not null default 1")
    private Boolean needLogin;          // 路由是否需要登录才允许访问

    @Column(columnDefinition = "bit not null default 1")
    private Boolean needPermission;     // 路由是否需要有权限才允许访问（needLogin = true 时才有意义）

    @Column
    private String permission;          // needPermission = true 时才有意义

    @Column(columnDefinition = "bit not null default 0")
    private Boolean showTag;            // 是否在标签栏显示

    @Column
    private String tagTitle;            // showTag = true 时才有意义

    // ----- 非持久化属性 -----
    @Transient
    private List<SysRoute> children = new ArrayList<>();

    public SysRoute(Long id, Long sortId, Long parentId, String name, String pathName,
                    String redirectPathName, String componentPath, Boolean needLogin,
                    Boolean needPermission, String permission, Boolean showTag, String tagTitle) {
        this.id = id;
        this.sortId = sortId;
        this.parentId = parentId;
        this.name = name;
        this.pathName = pathName;
        this.redirectPathName = redirectPathName;
        this.componentPath = componentPath;
        this.needLogin = needLogin;
        this.needPermission = needPermission;
        this.permission = permission;
        this.showTag = showTag;
        this.tagTitle = tagTitle;
    }
}
