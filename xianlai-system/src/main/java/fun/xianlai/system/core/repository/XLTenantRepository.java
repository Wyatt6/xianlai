package fun.xianlai.system.core.repository;

import fun.xianlai.system.core.model.entity.XLTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author WyattLau
 */
@Repository
public interface XLTenantRepository extends JpaRepository<XLTenant, Long> {
    XLTenant findByCode(String code);

    XLTenant findByDomain(String domain);
}
