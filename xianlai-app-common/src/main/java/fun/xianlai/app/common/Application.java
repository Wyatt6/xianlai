package fun.xianlai.app.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author WyattLau
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "fun.xianlai")
@EnableDiscoveryClient
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info("--------------- 微服务 xianlai-app-common 已启动 ---------------");
    }
}
