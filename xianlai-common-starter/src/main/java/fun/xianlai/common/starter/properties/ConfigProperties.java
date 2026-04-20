package fun.xianlai.common.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author WyattLau
 */
@Data
@Component
@ConfigurationProperties("xianlai.config")
public class ConfigProperties {
    private boolean enabled = false;
    private long globalConfigVersionPollRate = 60L;
}
