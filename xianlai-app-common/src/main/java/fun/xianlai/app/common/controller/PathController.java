package fun.xianlai.app.common.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import fun.xianlai.app.common.model.entity.SysPath;
import fun.xianlai.app.common.model.form.SysPathCondition;
import fun.xianlai.app.common.service.PathService;
import fun.xianlai.core.annotation.ApiLog;
import fun.xianlai.core.response.RetResult;
import fun.xianlai.core.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/path")
public class PathController {
    @Autowired
    private PathService pathService;

    @ApiLog("新增路径")
    @SaCheckLogin
    @SaCheckPermission("path:add")
    @PostMapping("/add")
    public RetResult add(@RequestBody SysPath form) {
        log.info("请求参数: {}", form);
        form.setName(StringUtil.trim(form.getName()));
        form.setPath(StringUtil.trim(form.getPath()));
        return new RetResult().success().setData(pathService.add(form));
    }

    @ApiLog("删除路径")
    @SaCheckLogin
    @SaCheckPermission("path:delete")
    @GetMapping("/delete")
    public RetResult delete(@RequestParam Long pathId) {
        log.info("请求参数: pathId=[{}]", pathId);
        pathService.delete(pathId);
        return new RetResult().success();
    }

    @ApiLog("修改路径")
    @SaCheckLogin
    @SaCheckPermission("path:edit")
    @PostMapping("/edit")
    public RetResult edit(@RequestBody SysPath form) {
        log.info("请求参数: {}", form);
        form.setName(StringUtil.trim(form.getName()));
        form.setPath(StringUtil.trim(form.getPath()));
        return new RetResult().success().setData(pathService.edit(form));
    }

    @ApiLog("重载路径缓存")
    @SaCheckLogin
    @SaCheckPermission("path:edit")
    @GetMapping("/reloadCache")
    public RetResult reloadCache() {
        pathService.cacheSysPaths();
        return new RetResult().success();
    }

    /**
     * 查询条件为空时查询全量数据
     * 页码<0或页大小<=0时不分页
     *
     * @param pageNum   页码
     * @param pageSize  页大小
     * @param condition 查询条件
     * @return {pageNum 页码, pageSize 页大小, totalPages 页码总数, totalElements 总条数, content 分页数据}
     */
    @ApiLog("条件查询路径分页")
    @SaCheckLogin
    @SaCheckPermission("path:query")
    @PostMapping("/getPageConditionally")
    public RetResult getPageConditionally(@RequestParam int pageNum,
                                          @RequestParam int pageSize,
                                          @RequestBody(required = false) SysPathCondition condition) {
        log.info("请求参数：pageNum=[{}], pageSize=[{}], condition=[{}]", pageNum, pageSize, condition);
        Page<SysPath> paths = pathService.getSysPathsByPageConditionally(pageNum, pageSize, condition);
        return new RetResult().success()
                .addData("pageNum", pageNum)
                .addData("pageSize", pageSize)
                .addData("totalPages", paths.getTotalPages())
                .addData("totalElements", paths.getTotalElements())
                .addData("content", paths.getContent());
    }
}
