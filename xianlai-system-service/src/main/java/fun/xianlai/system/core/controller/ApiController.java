package fun.xianlai.system.core.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {
//    @Autowired
//    private ApiService apiService;
//
//    @ApiLog("新增接口")
//    @SaCheckLogin
//    @SaCheckPermission("api:add")
//    @PostMapping("/add")
//    public RetResult add(@RequestBody XLApi form) {
//        log.info("请求参数: {}", form);
//        BeanUtils.trimString(form);
//        return new RetResult().success().setData(apiService.add(form));
//    }
//
//    @ApiLog("删除接口")
//    @SaCheckLogin
//    @SaCheckPermission("api:delete")
//    @GetMapping("/delete")
//    public RetResult delete(@RequestParam Long apiId) {
//        log.info("请求参数: apiId=[{}]", apiId);
//        apiService.delete(apiId);
//        return new RetResult().success();
//    }
//
//    @ApiLog("修改接口")
//    @SaCheckLogin
//    @SaCheckPermission("api:edit")
//    @PostMapping("/edit")
//    public RetResult edit(@RequestBody XLApi form) {
//        log.info("请求参数: {}", form);
//        BeanUtils.trimString(form);
//        return new RetResult().success().setData(apiService.edit(form));
//    }
//
//    @ApiLog("重载接口缓存")
//    @SaCheckLogin
//    @SaCheckPermission("api:edit")
//    @GetMapping("/reloadCache")
//    public RetResult reloadCache() {
//        apiService.cacheApis();
//        return new RetResult().success();
//    }
//
//    /**
//     * 查询条件为空时查询全量数据
//     * 页码<0或页大小<=0时不分页
//     *
//     * @param pageNum   页码
//     * @param pageSize  页大小
//     * @param condition 查询条件
//     * @return {pageNum 页码, pageSize 页大小, totalPages 页码总数, totalElements 总条数, content 分页数据}
//     */
//    @ApiLog("条件查询接口分页")
//    @SaCheckLogin
//    @SaCheckPermission("api:query")
//    @PostMapping("/getPageConditionally")
//    public RetResult getPageConditionally(@RequestParam int pageNum,
//                                          @RequestParam int pageSize,
//                                          @RequestBody(required = false) XLApi condition) {
//        log.info("请求参数：pageNum=[{}], pageSize=[{}], condition=[{}]", pageNum, pageSize, condition);
//        Page<XLApi> apis = apiService.getPageConditionally(pageNum, pageSize, condition);
//        return new RetResult().success()
//                .addData("pageNum", pageNum)
//                .addData("pageSize", pageSize)
//                .addData("totalPages", apis.getTotalPages())
//                .addData("totalElements", apis.getTotalElements())
//                .addData("content", apis.getContent());
//    }
}
