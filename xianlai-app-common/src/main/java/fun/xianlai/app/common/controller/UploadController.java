package fun.xianlai.app.common.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import fun.xianlai.core.annotation.ApiLog;
import fun.xianlai.core.response.RetResult;
import fun.xianlai.core.utils.file.FileUploadUtils;
import fun.xianlai.core.utils.file.FileUtils;
import fun.xianlai.core.utils.file.MimeTypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author WyattLau
 */
@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {
    private static final String SAVE_PATH = "./upload/avatar/";

    @ApiLog("上传头像图片")
    @PostMapping("/avatar")
    @SaCheckLogin
    public RetResult uploadAvatar(@RequestBody MultipartFile file) {
        Long userId = StpUtil.getLoginIdAsLong();
        String filename = FileUploadUtils.uploadFile(file, 500 * FileUtils.ONE_KB,
                SAVE_PATH, "avatar_", null, null, MimeTypeUtils.IMAGE_EXTENSION);
        return new RetResult().success()
                .addData("userId", userId)
                .addData("filename", filename);
    }
}
