package fun.xianlai.system.service.core.repository;

import fun.xianlai.system.service.core.entity.XLTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author WyattLau
 */
@Repository
public interface XLTenantRepository extends JpaRepository<XLTenant, Long> {
    Optional<XLTenant> findByCode(String code);

    Optional<XLTenant> findByDomain(String domain);
}
