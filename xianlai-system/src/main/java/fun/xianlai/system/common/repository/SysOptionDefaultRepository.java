package fun.xianlai.system.common.repository;

import fun.xianlai.system.common.model.entity.SysOptionDefault;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author WyattLau
 */
@Repository
public interface SysOptionDefaultRepository extends JpaRepository<SysOptionDefault, Long> {
    List<SysOptionDefault> findByScopeAndFrontLoad(String scope, Boolean frontLoad);

    List<SysOptionDefault> findByScope(String scope);

    Optional<SysOptionDefault> findByOptionKey(String optionKey);
}
