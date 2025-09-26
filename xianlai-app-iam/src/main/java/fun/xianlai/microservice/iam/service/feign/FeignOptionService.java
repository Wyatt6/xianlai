package fun.xianlai.microservice.iam.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * @author WyattLau
 */
@FeignClient(name = "xianlai-microservice-common", path = "/feign/option")
public interface FeignOptionService {
    @GetMapping("/getCertainBackLoadSysOptionValueFromCache")
    Optional<Object> getCertainBackLoadSysOptionValueFromCache(@RequestParam("key") String key);
}
