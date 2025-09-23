package fun.xianlai.microservice.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author WyattLau
 */
@Data
@Component
@ConfigurationProperties("datasource.redis.lettuce.pool")
public class LettucePoolProperties {
    private int minIdle = 1;
    private int maxIdle = 10;
    private int maxTotal = 10;
    private long maxWait = -1L;
}
