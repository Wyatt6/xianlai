package fun.xianlai.app.common.controller.feign;

import fun.xianlai.app.common.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author WyattLau
 */
@RestController
@RequestMapping("/feign/option")
public class FeignOptionController {
    @Autowired
    private OptionService optionService;

//    @FeignProducerLog("从缓存获取某个加载到后端的系统参数值")
    @GetMapping("/getCertainBackLoadSysOptionValueFromCache")
    public Optional<Object> getCertainBackLoadSysOptionValueFromCache(@RequestParam("key") String key) {
        return optionService.getCertainBackLoadSysOptionValueFromCache(key);
    }
}
