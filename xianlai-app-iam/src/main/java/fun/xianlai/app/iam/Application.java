package fun.xianlai.app.iam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author WyattLau
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "fun.xianlai")
@EnableDiscoveryClient
@EnableFeignClients
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info("--------------- 微服务 xianlai-app-iam 已启动 ---------------");
    }
}
