package fun.xianlai.app.iam.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author WyattLau
 */
@Data
@Component
@ConfigurationProperties("datasource.mysql")
public class MysqlProperties {
    private String host;
    private String port;
    private String db;
    private String username;
    private String password;
    private String publicKey;
}
