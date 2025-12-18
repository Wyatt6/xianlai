package fun.xianlai.mod.toolkit.service.impl;

import fun.xianlai.core.annotation.ServiceLog;
import fun.xianlai.core.utils.bean.BeanUtils;
import fun.xianlai.mod.toolkit.model.entity.SecretCode;
import fun.xianlai.mod.toolkit.repository.SecretCodeRepository;
import fun.xianlai.mod.toolkit.service.CodebookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author WyattLau
 */
@Slf4j
@Service
public class CodebookServiceImpl implements CodebookService {
    @Autowired
    private SecretCodeRepository secretCodeRepository;

    @Override
    @ServiceLog("条件查询密码本分页")
    public Page<SecretCode> getPageConditionally(int pageNum, int pageSize, SecretCode condition) {
        Long tenant = BeanUtils.getFieldValue(condition, "tenant", Long.class);
        String category = BeanUtils.getFieldValue(condition, "category", String.class);
        String title = BeanUtils.getFieldValue(condition, "title", String.class);

        Sort sort = Sort.by(Sort.Order.asc("sortId"), Sort.Order.asc("title"));
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
