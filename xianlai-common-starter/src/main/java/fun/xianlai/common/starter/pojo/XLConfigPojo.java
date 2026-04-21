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
public class XLConfigPojo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long belongId;

    private String level;

    private String configKey;

    private String configValue;

    private String valueType;       // 取值见： EConfigValueType

    private Boolean enabled;

    private Boolean frontLoad;

    private String name;

    private String remark;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;
}
