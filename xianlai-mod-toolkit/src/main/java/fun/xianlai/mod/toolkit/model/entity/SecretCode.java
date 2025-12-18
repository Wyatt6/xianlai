package fun.xianlai.mod.toolkit.model.entity;

import fun.xianlai.core.utils.bean.PrimaryKeyGenerator;
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
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_toolkit_secret_code", indexes = {
        @Index(columnList = "sortId, title"),
})
public class SecretCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PK_generator")
    @GenericGenerator(name = "PK_generator", type = PrimaryKeyGenerator.class)
    private Long id;

    @Column(columnDefinition = "bigint not null default 0")
    private Long sortId;

    @Column
    private String category;

    @Column
    private String title;

    @Column
    private String username;

    @Column
    private String code;

    @Column
    private String tips;

    @Column(columnDefinition = "bit not null default 0")
    private Boolean twoFAS;

    @Column(columnDefinition = "bit not null default 0")
    private Boolean appleId;

    @Column(columnDefinition = "bit not null default 0")
    private Boolean wechat;

    @Column(columnDefinition = "bit not null default 0")
    private Boolean alipay;

    @Column
    private String phone;

    @Column
    private String email;

    @Column(length = 1000)
    private String remark;

    @Column(columnDefinition = "bit not null default 0")
    private Boolean isDeleted;
}
