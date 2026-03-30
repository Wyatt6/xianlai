package fun.xianlai.common.starter.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author WyattLau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XLTenantPOJO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;                            // 主键ID
    private String code;                        // 租户编码（唯一）
    private String domain;                      // 租户独立域名
    private String status;                      // 租户状态
    private LocalDateTime expireTime;           // 租户过期时间
    private LocalDateTime configUpdateTime;     // 租户配置最后更新时间
    private String logo;                        // 租户Logo
    private String displayName;                 // 租户显示名称
    private String contactName;                 // 租户联系人
    private String contactGender;               // 租户联系人性别
    private String contactPhone;                // 租户联系人手机号
    private String contactEmail;                // 租户联系人电子邮箱
    private LocalDateTime createAt;             // 记录创建时间
    private LocalDateTime updateAt;             // 记录更新时间
}
