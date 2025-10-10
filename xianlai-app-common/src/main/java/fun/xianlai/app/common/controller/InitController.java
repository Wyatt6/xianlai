package fun.xianlai.app.common.controller;

import fun.xianlai.app.common.service.OptionService;
import fun.xianlai.app.common.service.PathService;
import fun.xianlai.basic.annotation.ControllerLog;
import fun.xianlai.basic.support.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/init")
public class InitController {
    @Autowired
    private OptionService optionService;
    @Autowired
    private PathService pathService;

    @ControllerLog("获取初始化数据")
    @GetMapping("/getInitData")
    public RetResult getInitData() {
        return new RetResult().success()
                .addData("options", optionService.getFrontLoadSysOptionsFromCache())
                .addData("paths", pathService.getSysPathsFromCache());
    }
}
