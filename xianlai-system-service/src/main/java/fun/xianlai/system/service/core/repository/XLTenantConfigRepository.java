package fun.xianlai.system.service.core.repository;

import fun.xianlai.system.service.core.entity.XLTenantConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WyattLau
 */
@Repository
public interface XLTenantConfigRepository extends JpaRepository<XLTenantConfig, Long> {
    List<XLTenantConfig> findByBelongToAndEnabled(Long belongTo, Boolean enabled);
}
