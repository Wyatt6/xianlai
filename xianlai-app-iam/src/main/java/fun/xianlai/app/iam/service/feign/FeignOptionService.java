package fun.xianlai.app.iam.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * @author WyattLau
 */
@FeignClient(contextId = "xianlai-app-common-option", name="xianlai-app-common", path = "/feign/option")
public interface FeignOptionService {
    @GetMapping("/readValueInString")
    Optional<String> readValueInString(@RequestParam String key);

    @GetMapping("/readValueInLong")
    Optional<Long> readValueInLong(@RequestParam String key);
}
