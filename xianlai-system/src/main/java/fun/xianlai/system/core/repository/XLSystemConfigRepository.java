package fun.xianlai.system.core.repository;

import fun.xianlai.system.core.model.entity.XLSystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WyattLau
 */
@Repository
public interface XLSystemConfigRepository extends JpaRepository<XLSystemConfig, Long> {
    List<XLSystemConfig> findByEnabled(Boolean enabled);
}
