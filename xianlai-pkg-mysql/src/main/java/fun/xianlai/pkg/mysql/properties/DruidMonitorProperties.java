package fun.xianlai.pkg.mysql.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author WyattLau
 */
@Data
@Component
@ConfigurationProperties("datasource.mysql.druid.monitor")
public class DruidMonitorProperties {
    private String username = "admin";
    private String password = "admin";
    private String whiteList = "";
    private String blackList = "";
}
