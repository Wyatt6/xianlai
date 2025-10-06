package fun.xianlai.app.common.feign.producer;

import fun.xianlai.app.common.service.OptionService;
import fun.xianlai.basic.support.RetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WyattLau
 */
@RestController
@RequestMapping("/feign/option")
public class FeignOptionController {
    @Autowired
    private OptionService optionService;

    @GetMapping("/readValueInString")
    public RetResult readValueInString(@RequestParam String key) {
        return new RetResult().writeFeignData(optionService.readValueInString(key));
    }

    @GetMapping("/readValueInLong")
    public RetResult readValueInLong(@RequestParam String key) {
        return new RetResult().writeFeignData(optionService.readValueInLong(key));
    }
}
