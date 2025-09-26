package fun.xianlai.app.gateway.service;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class NacosConfigService {
    @Value("${nacos.server-addr}")
    private String SERVER_ADDR;
    @Value("${nacos.namespace}")
    private String NAMESPACE;
    @Value("${nacos.username}")
    private String USERNAME;
    @Value("${nacos.password}")
    private String PASSWORD;

    public ConfigService initConfigService() {
        try {
            log.info("初始化ConfigService...");
            Properties p = new Properties();
            if (SERVER_ADDR != null) {
                p.setProperty("serverAddr", SERVER_ADDR);
                log.info("serverAddr = {}", SERVER_ADDR);
            }
            if (NAMESPACE != null) {
                p.setProperty("namespace", NAMESPACE);
                log.info("namespace = {}", NAMESPACE);
            }
            if (USERNAME != null) {
                p.setProperty("username", USERNAME);
                log.info("username = {}", USERNAME);
            }
            if (PASSWORD != null) {
                p.setProperty("password", PASSWORD);
                log.info("password = {}", PASSWORD);
            }
            log.info("ConfigService已初始化");
            return NacosFactory.createConfigService(p);
        } catch (NacosException e) {
            log.error("初始化ConfigService异常");
            log.error("{}", e.getMessage());
            return null;
        }
    }
}
