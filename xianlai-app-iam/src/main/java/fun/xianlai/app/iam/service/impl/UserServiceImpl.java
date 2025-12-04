package fun.xianlai.app.iam.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import fun.xianlai.app.iam.model.entity.other.Profile;
import fun.xianlai.app.iam.model.entity.other.UserInfo;
import fun.xianlai.app.iam.model.entity.rbac.Permission;
import fun.xianlai.app.iam.model.entity.rbac.Role;
import fun.xianlai.app.iam.model.entity.rbac.User;
import fun.xianlai.app.iam.model.entity.rbac.UserRole;
import fun.xianlai.app.iam.model.form.UserCondition;
import fun.xianlai.app.iam.repository.PermissionRepository;
import fun.xianlai.app.iam.repository.ProfileRepository;
import fun.xianlai.app.iam.repository.RoleRepository;
import fun.xianlai.app.iam.repository.UserRepository;
import fun.xianlai.app.iam.repository.UserRoleRepository;
import fun.xianlai.app.iam.service.PermissionService;
import fun.xianlai.app.iam.service.RoleService;
import fun.xianlai.app.iam.service.UserService;
import fun.xianlai.core.annotation.ServiceLog;
import fun.xianlai.core.annotation.SimpleServiceLog;
import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.feign.consumer.FeignOptionService;
import fun.xianlai.core.response.DataMap;
import fun.xianlai.core.utils.bean.BeanUtils;
import fun.xianlai.core.utils.password.PasswordUtils;
import fun.xianlai.core.utils.file.FileUploadUtils;
import fun.xianlai.core.utils.file.FileUtils;
import fun.xianlai.core.utils.file.FilenameUtils;
import fun.xianlai.core.utils.file.MimeTypeUtils;
import fun.xianlai.core.utils.time.DateUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
}
