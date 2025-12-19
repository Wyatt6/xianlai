package fun.xianlai.mod.toolkit.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import fun.xianlai.core.annotation.ServiceLog;
import fun.xianlai.core.exception.SysException;
import fun.xianlai.core.response.DataMap;
import fun.xianlai.core.utils.bean.BeanUtils;
import fun.xianlai.mod.toolkit.model.entity.SecretCode;
import fun.xianlai.mod.toolkit.repository.SecretCodeRepository;
import fun.xianlai.mod.toolkit.service.CodebookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class CodebookServiceImpl implements CodebookService {
    @Autowired
    private SecretCodeRepository secretCodeRepository;

    @Override
    @ServiceLog("新增密码条目")
    @Transactional
    public DataMap add(SecretCode secretCode) {
        try {
            secretCode.setId(null);
            secretCode.setTenant(StpUtil.getLoginIdAsLong());
            SecretCode savedCode = secretCodeRepository.save(secretCode);
            Long rowNum = secretCodeRepository.findRowNumById(savedCode.getId());
            DataMap result = new DataMap();
            result.put("secretCode", savedCode);
            result.put("rowNum", rowNum);
            return result;
        } catch (DataIntegrityViolationException e) {
            throw new SysException("密码条目已存在");
        }
    }

    @Override
    @ServiceLog("条件查询密码本分页")
    public Page<SecretCode> getPageConditionally(int pageNum, int pageSize, SecretCode condition) {
        Long tenant = BeanUtils.getFieldValue(condition, "tenant", Long.class);
        String category = BeanUtils.getFieldValue(condition, "category", String.class);
        String title = BeanUtils.getFieldValue(condition, "title", String.class);

        Sort sort = Sort.by(
                Sort.Order.asc("category"),
                Sort.Order.asc("sortId"),
                Sort.Order.asc("title")
        );
        if (pageNum >= 0 && pageSize > 0) {
            log.info("分页查询");
            Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
            return secretCodeRepository.findConditionally(tenant, category, title, pageable);
        } else {
            log.info("全表查询");
            return secretCodeRepository.findConditionally(tenant, category, title, Pageable.unpaged(sort));
        }
    }
}
