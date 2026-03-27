package fun.xianlai.system.core.repository;

import fun.xianlai.system.core.model.entity.XLTenantConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WyattLau
 */
@Repository
public interface XLTenantConfigRepository extends JpaRepository<XLTenantConfig, Long> {
    List<XLTenantConfig> findByScopeIdAndEnabled(Long scopeId, Boolean enabled);
}
