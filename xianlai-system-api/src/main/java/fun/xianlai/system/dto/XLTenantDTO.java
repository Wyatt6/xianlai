package fun.xianlai.system.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author WyattLau
 */
@Data
public class XLTenantDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 租户编码（唯一）
     */
    private String code;

    /**
     * 租户独立域名
     */
    private String domain;

    /**
     * 租户状态 ETenantStatus
     */
    private String status;

    /**
     * 租户过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 租户配置最后更新时间
     */
    private LocalDateTime configUpdateTime;

    /**
     * 租户Logo
     */
    private String logo;

    /**
     * 租户显示名称
     */
    private String displayName;

    /**
     * 租户联系人
     */
    private String contactName;

    /**
     * 租户联系人性别 EGender
     */
    private String contactGender;

    /**
     * 租户联系人手机号
     */
    private String contactPhone;

    /**
     * 租户联系人电子邮箱
     */
    private String contactEmail;

    /**
     * 创建时间
     */
    private LocalDateTime createAt;

    /**
     * 更新时间
     */
    private LocalDateTime updateAt;
}
